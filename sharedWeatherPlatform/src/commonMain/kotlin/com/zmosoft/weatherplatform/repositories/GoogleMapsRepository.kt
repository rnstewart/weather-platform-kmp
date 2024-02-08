package com.zmosoft.weatherplatform.repositories

import com.zmosoft.weatherplatform.api.models.response.geo.AutocompletePlacesData
import com.zmosoft.weatherplatform.utils.BackgroundDispatcher
import kotlinx.coroutines.withContext

class GoogleMapsRepository: RepositoryBase() {
    suspend fun searchLocation(
        input: String
    ): Result<List<AutocompletePlacesData.Prediction>> {
        return withContext (BackgroundDispatcher) {
            val response = googleMapsService.placesAutoComplete(
                input = input,
                latitude = null,
                longitude = null
            )

            val error = response.error
            if (error == null) {
                Result.success(response.data?.predictions ?: listOf())
            } else {
                Result.failure(error.throwable)
            }
        }
    }

    suspend fun autocompleteResultSelected(
        location: AutocompletePlacesData.Prediction
    ): Result<Pair<Double, Double>?> {
        return withContext (BackgroundDispatcher) {
            val placeId = location.placeId
            if (placeId?.isNotEmpty() == true) {
                val response = googleMapsService.placeDetails(placeId = placeId)
                val error = response.error
                if (error == null) {
                    val locationResult = response.data?.result?.geometry?.location
                    val latitude = locationResult?.latitude
                    val longitude = locationResult?.longitude
                    Result.success(
                        if (latitude != null && longitude != null) {
                            Pair(
                                latitude,
                                longitude
                            )
                        } else {
                            null
                        }
                    )
                } else {
                    Result.failure(error.throwable)
                }
            } else {
                Result.success(null)
            }
        }
    }
}
