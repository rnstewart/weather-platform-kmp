package com.zmosoft.weatherplatform.repositories.data

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.geo.PlaceDetailsResponse

data class GoogleMapsData(
    val autocompletePredictions: List<AutocompletePlacesData.Prediction> = listOf(),
    val placeDetails: PlaceDetailsResponse.Result? = null,
    val loading: Boolean = false
)