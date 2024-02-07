package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.api.models.response.geo.PlaceDetailsResponse
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import com.zmosoft.weatherplatform.utils.NetworkDispatcher
import kotlinx.coroutines.withContext

class GoogleMapsRepository: RepositoryBase() {
    suspend fun searchLocation(
        input: String
    ): List<AutocompletePlacesData.Prediction> {
        return withContext (BackgroundDispatcher) {
            val response = googleMapsService.placesAutoComplete(
                input = input,
                latitude = null,
                longitude = null
            )

            setError(response.error?.error ?: "")
            response.data?.predictions ?: listOf()
        }
    }

    suspend fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ): Pair<Double, Double>? {
        return withContext (BackgroundDispatcher) {
            val placeId = location.placeId
            if (placeId?.isNotEmpty() == true) {
                val response = googleMapsService.placeDetails(placeId = placeId)
                val locationResult = response.data?.result?.geometry?.location
                val latitude = locationResult?.latitude
                val longitude = locationResult?.longitude
                if (latitude != null && longitude != null) {
                    Pair(
                        latitude,
                        longitude
                    )
                } else {
                    setError(response.error?.error ?: "")
                    null
                }
            } else {
                null
            }
        }
    }

    suspend fun placeDetails(
        placeId: String,
        fields: String? = "address_component,name,geometry"
    ): PlaceDetailsResponse.Result? {
        return withContext (NetworkDispatcher) {
            val response = googleMapsService.placeDetails(placeId = placeId, fields = fields)

            setError(response.error?.error ?: "")
            response.data?.result
        }
    }
}
