package com.zmosoft.weatherplatform.api.models.response.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodeData(
    val results: List<Results>? = null,
    val status: String? = null
) {
    val localityName: String?
        get() {
            val name = results?.getOrNull(0)?.run {
                val cityStr = addressComponents?.find {
                    (it.types?.contains("political") == true && it.types.contains("locality"))
                }?.longName

                val stateStr = addressComponents?.find {
                    (it.types?.contains("political") == true && it.types.contains("administrative_area_level_1"))
                }?.shortName

                if (cityStr?.isNotEmpty() == true && stateStr?.isNotEmpty() == true) {
                    "$cityStr, $stateStr"
                } else if (cityStr?.isNotEmpty() == true) {
                    cityStr
                } else if (stateStr?.isNotEmpty() == true) {
                    stateStr
                } else {
                    null
                }
            }

            return name
        }

    @Serializable
    data class Results(
        @SerialName("address_components")
        val addressComponents: List<AddressComponent>? = null,
        @SerialName("formatted_address")
        val formattedAddress: String? = null,
        val geometry: Geometry? = null,
        @SerialName("place_id")
        val placeId: String? = null,
        @SerialName("plus_code")
        val plusCode: PlusCode? = null,
        val types: List<String>? = null
    ) {
        @Serializable
        data class PlusCode(
            @SerialName("compound_code")
            val compoundCode: String? = null,
            @SerialName("global_code")
            val globalCode: String? = null
        )
    }
}
