package com.zmosoft.weatherplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform