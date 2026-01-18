package com.drake.droidblox.apiservice.models.discord

import kotlinx.serialization.SerialName

data class DiscordExternalAssetMP(
    @SerialName("external_assets_path")
    val externalAssetPath: String
)