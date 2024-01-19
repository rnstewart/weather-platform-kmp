package com.zmosoft.weatherplatform.api.models

import com.zmosoft.weatherplatform.utils.base64encoded

data class Auth(
    val username: String? = null,
    val password: String? = null,
    val authHeader: String = ""
) {
    fun generateAuthHeader(): Auth {
        return Auth(
            authHeader = if (username?.isNotEmpty() == true && password?.isNotEmpty() == true)
                "${username.trim().base64encoded}:${password.trim().base64encoded}"
                    .base64encoded
            else
                ""
        )
    }

    fun hasAuthHeader(): Boolean {
        return (authHeader.isNotEmpty())
    }

    fun clearAuthHeader(): Auth {
        return Auth()
    }
}