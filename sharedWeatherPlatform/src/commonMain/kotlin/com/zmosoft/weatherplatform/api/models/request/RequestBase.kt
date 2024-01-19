package com.zmosoft.weatherplatform.api.models.request

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

abstract class RequestBase(
    open val method: HttpMethod,
    open val path: String,
    open val contentType: ContentType = ContentType.Application.Json,
    open val pathParams: List<Any> = listOf(),
    open val queryParams: Map<String, Any?> = mapOf(),
    open val bodyData: Any? = null,
    open val expectSuccess: Boolean = true,
    open val requireAuth: Boolean = true,
    private val pathBase: String = ""
) {
    val fullPath: String
        get() = "$pathBase/$path" + if (pathParams.isNotEmpty())
            "/" + pathParams.map { it.toString().trim() }.filter { it.isNotEmpty() }.joinToString("/")
        else
            ""

    val params: HttpRequestBuilder.() -> Unit = {
        queryParams.forEach { (key, value) ->
            val valString = value?.toString()
            if (valString?.isNotEmpty() == true) {
                parameter(key, valString)
            }
        }
    }
}