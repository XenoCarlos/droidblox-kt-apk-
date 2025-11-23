package com.drake.droidblox.backend.apis.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RobloxUser(
    val name: String,
    val displayName: String,
    @SerialName("hasVerifiedBadge")
    val isVerfied: Boolean
)
