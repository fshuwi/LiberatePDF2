/**
 * LiberatePDF2 Microservice
 * Microservice for LiberatePDF2
 *
 * The version of the OpenAPI document: 0.1
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.debuglevel.liberatepdf2.restclient.models

import com.squareup.moshi.Json

/**
 *
 * @param lastModified
 * @param length
 * @param mediaType
 * @param inputStream
 */

data class StreamedFile(
    @Json(name = "lastModified")
    val lastModified: kotlin.Long? = null,
    @Json(name = "length")
    val length: kotlin.Long? = null,
    @Json(name = "mediaType")
    val mediaType: MediaType? = null,
    @Json(name = "inputStream")
    val inputStream: kotlin.Any? = null
)
