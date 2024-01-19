package com.zmosoft.weatherplatform.api.models.request

import io.ktor.http.*

class AutocompletePlacesRequest(
    input: String,
    latitude: Double? = null,
    longitude: Double? = null,
    apiKey: String
): GoogleMapsRequestBase(
    path = "place/autocomplete/json",
    method = HttpMethod.Get,
    requireAuth = false,
    queryParams = mapOf(
        "input" to input.trim(),
        "location" to if (latitude != null && longitude != null)
            "$latitude,$longitude"
        else
            null,
        "key" to apiKey
    )
)
