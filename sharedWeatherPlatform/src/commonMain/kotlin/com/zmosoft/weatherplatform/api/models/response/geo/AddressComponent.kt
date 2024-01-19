package com.zmosoft.weatherplatform.api.models.response.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponent(
    @SerialName("long_name")
    val longName: String? = null,
    @SerialName("short_name")
    val shortName: String? = null,
    val types: List<String>? = null
)