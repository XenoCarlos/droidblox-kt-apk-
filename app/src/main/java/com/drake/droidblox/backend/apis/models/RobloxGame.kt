package com.drake.droidblox.backend.apis.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RobloxGame(
    val name: String,
    val creator: User,
    @SerialName("rootPlaceId")
    val placeId: Long,
    @SerialName("id")
    val universeId: Long
)
