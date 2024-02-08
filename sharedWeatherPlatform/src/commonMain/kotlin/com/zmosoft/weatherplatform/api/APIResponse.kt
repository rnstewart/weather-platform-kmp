package com.zmosoft.weatherplatform.api

import com.zmosoft.weatherplatform.api.models.response.ResponseBase
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

data class APIResponse<T : ResponseBase>(
    val data: T? = null,
    val statusCode: HttpStatusCode,
    val message: String = statusCode.description
) {
    data class APIError(val httpCode: HttpStatusCode, val error: String = httpCode.description) {
        val throwable: Throwable
            get() = Exception(error)
    }

    val success: Boolean
        get() = statusCode.isSuccess()

    val notFound: Boolean
        get() = statusCode == HttpStatusCode.NotFound

    val error: APIError?
        get() = if (statusCode.isSuccess()) {
            null
        } else {
            val errorMsg = data?.message
            APIError(
                httpCode = statusCode,
                error = if (errorMsg?.isNotEmpty() == true)
                    errorMsg
                else
                    message
            )
        }

    companion object {
        fun <T: ResponseBase> invalidContentResponse(valuesMissing: List<String>? = null): APIResponse<T> {
            return APIResponse(
                data = null,
                statusCode = HttpStatusCode.BadRequest,
                message = if (valuesMissing?.isNotEmpty() == true) {
                    "Values Missing: ${valuesMissing.joinToString(" ")}"
                } else {
                    "Values Missing"
                }
            )
        }

        inline fun <reified T : ResponseBase> unauthorizedResponse(): APIResponse<T> {
            val httpCode = HttpStatusCode.Unauthorized
            return APIResponse(
                data = null,
                statusCode = httpCode,
                message = httpCode.description
            )
        }

        inline fun <reified T : ResponseBase> exceptionResponse(e: Exception): APIResponse<T> {
            val httpCode = if (e is ClientRequestException) {
                e.response.status
            } else {
                HttpStatusCode.BadRequest
            }
            val eMessage = e.message
            return APIResponse(
                data = null,
                statusCode = httpCode,
                message = if (eMessage?.isNotEmpty() == true)
                    eMessage
                else
                    httpCode.description
            )
        }
    }
}