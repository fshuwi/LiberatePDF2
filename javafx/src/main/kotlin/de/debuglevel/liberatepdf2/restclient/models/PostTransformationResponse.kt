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
 * @param id 
 * @param originalFilename 
 * @param finished 
 * @param failed 
 * @param errorMessage 
 */

data class PostTransformationResponse (
    @Json(name = "id")
    val id: java.util.UUID? = null,
    @Json(name = "originalFilename")
    val originalFilename: kotlin.String? = null,
    @Json(name = "finished")
    val finished: kotlin.Boolean? = null,
    @Json(name = "failed")
    val failed: kotlin.Boolean? = null,
    @Json(name = "errorMessage")
    val errorMessage: kotlin.String? = null
)

