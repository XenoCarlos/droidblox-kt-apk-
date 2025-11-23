package com.drake.droidblox.backend.sharedprefs.playsession.models

import kotlinx.serialization.Serializable

@Serializable
data class PlaySession(
    val universeId: Long,
    val playedAt: Long,
    val leftAt: Long,
    val deeplink: String
)