package com.drake.droidblox.apiservice.models.rovalra

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoValraIpLocation(
    val city: String,
    val region: String,
    @SerialName("country_code")
    val country: String
)

@Serializable
data class RawRoValraIpLocation(
    val location: RoValraIpLocation
)