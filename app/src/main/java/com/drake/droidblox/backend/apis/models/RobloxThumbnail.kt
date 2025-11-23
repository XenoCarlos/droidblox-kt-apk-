package com.drake.droidblox.backend.apis.models

import kotlinx.serialization.Serializable

@Serializable
data class RobloxThumbnail(
    val targetId: Long,
    val type: String,
    val size: String,
    val isCircular: Boolean
)
