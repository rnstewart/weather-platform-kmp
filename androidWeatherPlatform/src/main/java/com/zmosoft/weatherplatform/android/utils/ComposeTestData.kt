package com.zmosoft.weatherplatform.android.utils

import com.zmosoft.weatherplatform.api.models.response.weather.WeatherDataResponse

object ComposeTestData {
    val weatherData = WeatherDataResponse(
        name = "Albuquerque",
        main = WeatherDataResponse.Main(
            temp = 283.0
        ),
        weather = listOf(
            WeatherDataResponse.Weather(
                main = "Clouds"
            )
        ),
        wind = WeatherDataResponse.Wind(
            speed = 12.3,
            deg = 51
        ),
        sys = WeatherDataResponse.Sys(
            sunrise = 1647754260,
            sunset = 1647799183
        )
    )
}