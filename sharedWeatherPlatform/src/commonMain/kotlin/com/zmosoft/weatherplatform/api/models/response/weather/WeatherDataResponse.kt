package com.zmosoft.weatherplatform.api.models.response.weather

import com.zmosoft.weatherplatform.api.models.response.ResponseBase
import com.zmosoft.weatherplatform.utils.*
import korlibs.time.DateFormat
import korlibs.time.DateTimeTz
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class WeatherDataResponse(
    var id: Long? = null,
    var name: String? = null,
    var coord: Coord? = null,
    var weather: List<Weather>? = null,
    var sys: Sys? = null,
    var base: String? = null,
    var main: Main? = null,
    var visibility: Double? = null,
    var wind: Wind? = null,
    var rain: Rain? = null,
    var dt: Long? = null,
    var timeZone: Long? = null
) : ResponseBase() {
    @Serializable
    data class Coord(
        var lon: Double? = null,
        var lat: Double? = null
    )

    @Serializable
    data class Weather(
        var id: Long? = null,
        var main: String? = null,
        var description: String? = null,
        var icon: String? = null
    )

    @Serializable
    data class Main(
        var temp: Double? = null,
        var pressure: Double? = null,
        var humidity: Double? = null,
        @SerialName("temp_min")
        var tempMin: Double? = null,
        @SerialName("temp_max")
        var tempMax: Double? = null
    ) {
        val tempFahrenheit: String
            get() = temp?.kelvinToFahrenheit()?.roundToInt()?.toString() ?: ""

    }

    @Serializable
    data class Wind(
        var speed: Double? = null,
        var deg: Int? = null
    ) {
        val windWithDirection: Pair<String, String>?
            get() = speed?.let { speedValue ->
                val dir = deg.directionString
                if (dir.isNotEmpty())
                    Pair(
                        StringFormat.formatDecimal(
                            speedValue,
                            decimalDigits = 1
                        ),
                        dir
                    )
                else
                    null
            }
    }

    @Serializable
    data class Rain(
        @SerialName("1h")
        var oneH: Double? = null
    )

    @Serializable
    data class Clouds(
        var all: Double? = null
    )

    @Serializable
    data class Sys(
        var type: Int? = null,
        var id: Long? = null,
        var country: String? = null,
        var sunrise: Long? = null,
        var sunset: Long? = null
    ) {
        val sunriseStr: String
            get() = sunrise?.let {
                DateTimeTz.fromUnix(it * 1000).format(DateFormat(Constants.TIME_FORMAT))
            } ?: ""

        val sunsetStr: String
            get() = sunset?.let { sunset ->
                DateTimeTz.fromUnix(sunset * 1000).format(DateFormat(Constants.TIME_FORMAT))
            } ?: ""

    }

    fun getIconUrl(density: Int): String {
        val icon = weather?.getOrNull(0)?.icon
        return if (icon?.isNotEmpty() == true) {
            "$ICON_URL_BASE$icon@${density}x.png"
        } else {
            ""
        }
    }

    val windWithDirection: Pair<String, String>?
        get() = wind?.windWithDirection

    val currentTempFahrenheit: String
        get() = main?.tempFahrenheit ?: ""

    val currentWeatherCondition: String
        get() = weather?.getOrNull(0)?.main ?: ""

    val sunriseStr: String
        get() = sys?.sunriseStr ?: ""

    val sunsetStr: String
        get() = sys?.sunsetStr ?: ""

    companion object {
        const val ICON_URL_BASE = "https://openweathermap.org/img/wn/"
    }
}