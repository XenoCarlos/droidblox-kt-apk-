package com.drake.droidblox.backend.activitywatcher.models

data class BSRpcAsset(
    val assetId: Long,
    val hoverText: String? = null,
    val clear: Boolean? = false,
    val reset: Boolean? = false
)
