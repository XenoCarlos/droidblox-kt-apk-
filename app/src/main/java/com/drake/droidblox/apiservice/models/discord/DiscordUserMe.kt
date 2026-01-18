package com.drake.droidblox.apiservice.models.discord

import kotlinx.serialization.Serializable

@Serializable
data class DiscordUserMe(
    val username: String
)
