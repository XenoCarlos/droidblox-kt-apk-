package com.drake.droidblox.apiservice.models.roblox

import kotlinx.serialization.Serializable

@Serializable
data class RobloxThumbnail(
    val targetId: Long,
    val type: String,
    val size: String,
    val isCircular: Boolean
)

@Serializable
data class RobloxThumbnailResponse(
    val imageUrl : String
)

@Serializable
data class RawRobloxThumbnailResponse(
    val data: List<RobloxThumbnailResponse>
)