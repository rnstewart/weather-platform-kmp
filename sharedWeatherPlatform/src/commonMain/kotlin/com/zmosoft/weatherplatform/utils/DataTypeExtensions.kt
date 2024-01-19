package com.zmosoft.weatherplatform.utils

fun Double.kelvinToFahrenheit(): Double {
    return ((this - 273.15) * (9.toDouble() / 5.toDouble())) + 32.toDouble()
}
