package com.zmosoft.weatherplatform.interfaces

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.data.SharedRepositories
import com.zmosoft.weatherplatform.repositories.WeatherRepository
import com.zmosoft.weatherplatform.repositories.interfaces.GoogleMapsInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SharedGoogleMapsInterface(
    scope: CoroutineScope?,
    private val sharedRepositories: SharedRepositories
): SharedInterfaceBase(scope = scope), GoogleMapsInterface {
    override fun placesAutoComplete(input: String, latitude: Double?, longitude: Double?) {
        coroutineScope.launch {
            sharedRepositories.googleMapsRepository.placesAutoComplete(
                input = input,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ) {
        coroutineScope.launch {
            sharedRepositories.googleMapsRepository.autocompleteResultSelected(
                location = location,
                weatherRepository = sharedRepositories.weatherRepository
            )
        }
    }

    override fun placeDetails(placeId: String, fields: String?) {
        coroutineScope.launch {
            sharedRepositories.googleMapsRepository.placeDetails(
                placeId = placeId,
                fields = fields
            )
        }
    }
}