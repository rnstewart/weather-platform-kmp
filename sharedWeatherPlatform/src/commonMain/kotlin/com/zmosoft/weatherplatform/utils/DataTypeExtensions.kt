package com.zmosoft.weatherplatform.utils

fun Double.kelvinToFahrenheit(): Double {
    return ((this - 273.15) * (9.toDouble() / 5.toDouble())) + 32.toDouble()
}

val Int?.directionString: String
    get() = this?.toDouble()?.let {
        when {
            (this >= 22.5 && this < 67.5) -> "NE"
            (this >= 67.5 && this < 112.5) -> "E"
            (this >= 112.5 && this < 157.5) -> "SE"
            (this >= 157.5 && this < 202.5) -> "S"
            (this >= 202.5 && this < 247.5) -> "SW"
            (this >= 247.5 && this < 292.5) -> "W"
            (this >= 292.5 && this < 337.5) -> "W"
            else -> "N"
        }
    } ?: ""
