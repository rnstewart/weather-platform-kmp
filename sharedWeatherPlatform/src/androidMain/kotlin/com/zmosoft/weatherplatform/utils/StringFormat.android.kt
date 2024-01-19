package com.zmosoft.weatherplatform.utils

import java.net.URLEncoder
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.Base64
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs

private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"

actual object StringFormat {
    actual fun formatPercent(value: Double, digits: Int): String {
        val format = DecimalFormat(
            buildString {
                append("#")
                if (digits > 0) {
                    append(".")
                }
                append(List(digits) { "0" }.joinToString(""))
                append("%")
            }
        )
        return if (abs(value) < 0.005) {
            // Necessary because formatting Double value of 0.0 returns "-%0
            format.format(0)
        } else {
            format.format(value)
        }
    }

    actual fun formatDecimal(value: Double, leadingDigits: Int, decimalDigits: Int): String {
        return DecimalFormat(
            buildString {
                if (leadingDigits > 0) {
                    append(List(leadingDigits + 1) { "0" }.joinToString(""))
                } else {
                    append("#")
                }
                if (decimalDigits > 0) {
                    append(".")
                }
                append(List(decimalDigits) { "0" }.joinToString(""))
            }
        ).format(value)
    }
}

actual fun String.md5(): String {
    val bytes = MessageDigest.getInstance("SHA-1").digest(this.toByteArray())
    return bytes.joinToString("") {
        "%02x".format(it)
    }
}

actual val String.urlEncode: String
    get() = URLEncoder.encode(this, "UTF-8")

actual fun String.sha1(keyString: String): String {
    val bytes = Mac.getInstance(HMAC_SHA1_ALGORITHM).run {
        init(SecretKeySpec(keyString.toByteArray(charset("UTF-8")), HMAC_SHA1_ALGORITHM))
        doFinal(toByteArray(charset("UTF-8")))
    }

    return Base64.getEncoder().encodeToString(bytes)
}

actual object StringUtils {
    actual val uuid: String
        get() = UUID.randomUUID().toString()
}