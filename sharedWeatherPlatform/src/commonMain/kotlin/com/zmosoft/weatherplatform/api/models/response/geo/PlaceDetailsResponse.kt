package com.zmosoft.weatherplatform.api.models.response.geo

import com.zmosoft.weatherplatform.api.models.response.ResponseBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class PlaceDetailsResponse(
    val result: Result? = null
): ResponseBase() {
    @Serializable
    data class Result(
        @SerialName("address_components")
        val addressComponents: List<AddressComponent>? = null,
        val geometry: Geometry? = null
    )
}
