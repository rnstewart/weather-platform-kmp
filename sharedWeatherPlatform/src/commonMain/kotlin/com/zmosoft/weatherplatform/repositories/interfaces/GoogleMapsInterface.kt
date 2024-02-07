package com.zmosoft.weatherplatform.repositories.interfaces

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData

interface GoogleMapsInterface {
    fun onLocationSearch(
        input: String
    )
    fun onLocationSelected(location: AutocompletePlacesData.Prediction)
}