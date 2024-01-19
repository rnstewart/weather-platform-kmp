package com.zmosoft.weatherplatform.api.models.response

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData


@kotlinx.serialization.Serializable
data class AutocompletePlacesResponse(
    val status: String? = null,
    val predictions: List<AutocompletePlacesData.Prediction>? = null
): ResponseBase()
