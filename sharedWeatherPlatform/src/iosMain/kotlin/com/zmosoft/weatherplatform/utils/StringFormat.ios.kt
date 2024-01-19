package com.zmosoft.weatherplatform.utils

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.Foundation.*
import kotlin.math.abs

actual object StringFormat {
    actual fun formatPercent(value: Double, digits: Int): String {
        val nsDigits = NSNumber(digits).unsignedIntegerValue
        val format = NSNumberFormatter().apply {
            locale = NSLocale.currentLocale
            numberStyle = NSNumberFormatterPercentStyle
            minimumFractionDigits = nsDigits
            maximumFractionDigits = nsDigits
        }

        return if (abs(value) < 0.005) {
            // Necessary because formatting Double value of 0.0 returns "-%0
            format.stringFromNumber(NSNumber(0)) ?: ""
        } else {
            format.stringFromNumber(NSNumber(value)) ?: ""
        }
    }

    actual fun formatDecimal(
        value: Double,
        leadingDigits: Int,
        decimalDigits: Int
    ): String {
        val nsDigits = NSNumber(decimalDigits).unsignedIntegerValue
        return NSNumberFormatter().apply {
            locale = NSLocale.currentLocale
            minimumFractionDigits = nsDigits
            maximumFractionDigits = nsDigits
            minimumIntegerDigits = NSNumber(
                if (leadingDigits > 0)
                    leadingDigits + 1
                else
                    0
            ).unsignedIntegerValue
        }.stringFromNumber(NSNumber(value)) ?: ""
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
actual fun String.md5(): String {
    val input = encodeToByteArray()
    val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)
    input.usePinned { inputPinned ->
        digest.usePinned { digestPinned ->
            CC_SHA256(inputPinned.addressOf(0), input.size.convert(), digestPinned.addressOf(0))
        }
    }
    return digest.joinToString(separator = "") { it.toString(16) }
}

actual val String.urlEncode: String
    get() {
        val unreservedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        val unreservedCharsSet = NSCharacterSet(
            NSCoder().apply {
                encodeObject(unreservedChars)
            }
        )
        return NSString(
            NSCoder().apply {
                encodeObject(this)
            }
        ).stringByAddingPercentEncodingWithAllowedCharacters(unreservedCharsSet).toString()
    }

actual fun String.sha1(keyString: String): String {
    return ""
}

actual object StringUtils {
    actual val uuid: String
        get() = NSUUID().UUIDString()
}