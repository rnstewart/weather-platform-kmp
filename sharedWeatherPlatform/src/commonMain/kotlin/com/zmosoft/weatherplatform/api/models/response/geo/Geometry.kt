package com.zmosoft.weatherplatform.api.models.response.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val location: Location? = null,
    @SerialName("location_type")
    val locationType: String? = null
) {
    @Serializable
    data class Location(
        @SerialName("lat")
        val latitude: Double? = null,
        @SerialName("lng")
        val longitude: Double? = null
    )

    @Serializable
    data class Viewport(
        val northeast: Location? = null,
        val southwest: Location? = null
    )
}
