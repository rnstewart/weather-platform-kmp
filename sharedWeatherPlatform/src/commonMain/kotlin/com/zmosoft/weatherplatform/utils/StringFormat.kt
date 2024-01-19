package com.zmosoft.weatherplatform.utils

expect object StringFormat {
    fun formatPercent(value: Double, digits: Int = 0): String
    fun formatDecimal(value: Double, leadingDigits: Int = 0, decimalDigits: Int = 0): String
}

expect object StringUtils {
    val uuid: String
}

expect fun String.sha1(keyString: String): String
expect fun String.md5(): String
expect val String.urlEncode: String
