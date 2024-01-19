package com.zmosoft.weatherplatform.utils

expect object PlatformInfo {
    fun appReference(): String
    fun appVersion(): String
    fun isDebug(): Boolean
}