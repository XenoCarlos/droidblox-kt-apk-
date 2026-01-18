package com.drake.droidblox.ui.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RailDestination(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Integrations : RailDestination(Routes.INTEGRATIONS, Icons.Default.Add, "Integrations")
    object FastFlags : RailDestination(Routes.FFLAGS, Icons.Default.Favorite, "Fast Flags")
    object About : RailDestination(Routes.ABOUT, Icons.Default.Info, "About")
}