package com.zmosoft.weatherplatform.android.utils

import android.content.Context
import com.zmosoft.weatherplatform.android.R
import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

val WeatherDataResponse.sunriseIcon: Int?
    get() = if (sunriseStr.isNotEmpty())
        R.drawable.ic_sunrise_32dp
    else
        null

val WeatherDataResponse.sunsetIcon: Int?
    get() = if (sunsetStr.isNotEmpty())
        R.drawable.ic_sunset_32dp
    else
        null

fun WeatherDataResponse.getWindStr(context: Context): String {
    return windWithDirection?.let {
        val speed = it.first
        val dir = it.second
        if (dir.isNotEmpty())
            context.getString(
                R.string.wind_info_direction,
                dir,
                speed
            )
        else
            context.getString(
                R.string.wind_info,
                speed
            )
    } ?: ""
}

fun WeatherDataResponse.getCurrentTempStr(context: Context?): String? {
    return context?.let {
        context.getString(
            R.string.temperature_x,
            currentTempFahrenheit
        )
    }
}

fun WeatherDataResponse.getWindIcon(context: Context): Int? {
    return if (getWindStr(context).isNotEmpty())
        R.drawable.ic_wind_32dp
    else
        null
}

fun WeatherDataResponse.getWeatherIconUrl(context: Context): String? {
    return context.getString(R.string.icon_factor).toIntOrNull()?.let { density ->
        getIconUrl(density)
    }
}

