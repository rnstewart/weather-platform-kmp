package com.zmosoft.weatherplatform.api

import kotlinx.serialization.Serializable

// The data for this class is pulled from the apiKeys.json file for both Android and iOS.
// To see how to set that up, look at the "resources/apiKeysSample.txt" file.
@Serializable
data class APIKeys(
    val openWeatherMap: OpenWeatherMap = OpenWeatherMap(),
    val googleMaps: GoogleMaps = GoogleMaps()
) {
    @Serializable
    data class OpenWeatherMap(
        val apiKey: String = "",
        val apiHost: String = ""
    )

    @Serializable
    data class GoogleMaps(
        val apiKey: String = "",
        val apiHost: String = ""
    )
}