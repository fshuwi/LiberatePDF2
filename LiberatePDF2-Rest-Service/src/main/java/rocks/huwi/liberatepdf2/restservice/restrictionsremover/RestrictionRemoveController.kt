package rocks.huwi.liberatepdf2.restservice.restrictionsremover

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.server.types.files.SystemFile
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import rocks.huwi.liberatepdf2.restservice.storage.StorageService
import rocks.huwi.liberatepdf2.restservice.storage.ZipService
import java.net.URI
import java.nio.file.Files
import java.util.*

//import javax.servlet.http.HttpServletResponse

@Controller("/api/v1/documents")
@Tag(name = "documents")
class RestrictionRemoveController(
    private val storageService: StorageService,
    private val restrictionsRemoverService: RestrictionsRemoverService,
    private val zipService: ZipService
) {
    private val logger = KotlinLogging.logger {}

    @Get("/zip{?id}")
    fun downloadZip(
        id: Array<UUID>?, // CAVEAT: multiple query
        //response: HttpServletResponse?
    ): HttpResponse<*> {
        logger.debug { "Received GET or HEAD request for multiple ${id?.size} documents ${id?.joinToString()}" }
        val zip = zipService.createZip(id!!)
        val filesystemResource = zip.toFile()

        return HttpResponse.ok(SystemFile(filesystemResource).attach("unrestricted PDFs.zip"))

//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .header("Content-Disposition", "attachment; filename=\"" + "unrestricted PDFs.zip" + "\"")
//            .body(filesystemResource)
    }

    @Get("/{documentId}")
    fun downloadUnrestricted(
        documentId: UUID,
    ): HttpResponse<*> {
        logger.debug { "Received GET or HEAD request for document $documentId" }

        val pdf = storageService.getItem(documentId)
        return if (pdf == null) {
            // no item found with this ID (because no request was assigned this ID by now)
            logger.debug { "No document with ID=$documentId found" }

            HttpResponse.notFound("No document found for ID=$documentId")
        } else if (!pdf.isDone) {
            // the request exists, but was not transformed by now
            logger.debug { "Document with ID=${documentId} found, but pdf.isDone=false (not processed by now)" }
            HttpResponse.status<Any>(HttpStatus.PROCESSING) // .status(HTTP_STATUS_IN_PROGRESS) // CAVEAT/TODO
                .body("The document was not processed by now. Please try again later.")
        } else if (!Files.exists(pdf.unrestrictedPath)) {
            // the request was transformed, but the file does not exist (somehow failed?)
            logger.debug { "Document with ID=${documentId} found, but no file exists" }
            HttpResponse.status<String>(HttpStatus.INTERNAL_SERVER_ERROR) // .status(HTTP_STATUS_FAILED) // CAVEAT/TODO
                .body("The document was processed, but produced no result. Maybe the password was wrong or another error occurred.")
        } else {
            // request should be okay
            val filename = storageService.getItem(documentId)!!.originalFilename
            val filesystemResource = pdf.unrestrictedPath!!.toFile()
            logger.debug { ("Document with ID=${documentId} found and set for delivery") }

            HttpResponse.ok(SystemFile(filesystemResource).attach(filename))

//            HttpResponse.ok().contentType(MediaType.APPLICATION_PDF)
//                .header("Content-Disposition", "attachment; filename=\"$filename\"")
//                .body(filesystemResource)
        }
    }

    @Post("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun uploadAndRemoveRestrictions(
        file: CompletedFileUpload,
        password: String,
        //restrictedPdf: PdfDTO,
        //uriComponentsBuilder: UriComponentsBuilder
    ): HttpResponse<String> {
        logger.debug { "POST / with file=$file, password=$password" }

        val pdf = storageService.store(file.filename, file.inputStream, password)
        pdf.password = password

        restrictionsRemoverService.removeRestrictionsAsync(pdf)
        val uri = URI("/documents/${pdf.id}")

        //return ResponseEntity.accepted().location(uriComponents.toUri()).body(pdf.id)
        val x = HttpResponse.accepted<String>(uri).body(pdf.id.toString())
        return x

        return HttpResponse.accepted(uri) // CAVEAT: maybe needs to return id, how?
        //return pdf.id
    }

    companion object {
        private const val HTTP_STATUS_FAILED = 560
        private const val HTTP_STATUS_IN_PROGRESS = 260
    }
}