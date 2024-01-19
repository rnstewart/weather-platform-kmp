package com.zmosoft.weatherplatform.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ResponseBase(
    val message: String? = null,
    @SerialName("cod")
    val code: String? = null
) {
    override fun toString(): String {
        return "ResponseBase(message = \"$message\", code=\"$code\")"
    }
}