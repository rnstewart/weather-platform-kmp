package com.zmosoft.weatherplatform.api

import com.zmosoft.weatherplatform.api.models.request.AutocompletePlacesRequest
import com.zmosoft.weatherplatform.api.models.request.PlaceDetailsRequest
import com.zmosoft.weatherplatform.api.models.response.AutocompletePlacesResponse
import com.zmosoft.weatherplatform.api.models.response.geo.PlaceDetailsResponse

class GoogleMapsService(
    private val apiKeys: APIKeys
): Api(
    baseUrl = apiKeys.googleMaps.apiHost
) {
    suspend fun placesAutoComplete(
        input: String,
        latitude: Double? = null,
        longitude: Double? = null
    ): APIResponse<AutocompletePlacesResponse> {
        return apiCall(
            AutocompletePlacesRequest(
                input = input,
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKeys.googleMaps.apiKey
            )
        )
    }

    suspend fun placeDetails(
        placeId: String,
        fields: String? = "address_component,name,geometry"
    ): APIResponse<PlaceDetailsResponse> {
        return apiCall(
            PlaceDetailsRequest(
                placeId = placeId,
                fields = fields,
                apiKey = apiKeys.googleMaps.apiKey
            )
        )
    }
}

//@GET("geocode/json")
//suspend fun geocodeByAddress(
//    @Query("address") address: String
//): Response<GeocodeData>
//
//@GET("geocode/json")
//suspend fun geocodeByLocation(
//    @Query("latlng") latlng: String
//): Response<GeocodeData>
//
