package com.zmosoft.weatherplatform.repositories.interfaces

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.repositories.WeatherRepository

interface GoogleMapsInterface {
    fun placesAutoComplete(
        input: String,
        latitude: Double? = null,
        longitude: Double? = null
    )
    fun autocompleteResultSelected(location: AutocompletePlacesData.Prediction)
    fun placeDetails(
        placeId: String,
        fields: String? = "address_component,name,geometry"
    )
}