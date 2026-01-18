package com.drake.droidblox.ui.view.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppNavigationRail(
    currentRoute: String = Routes.INTEGRATIONS,
    onNavigate: ((String) -> Unit) = {}
) {
    val destinations = listOf(
        RailDestination.Integrations,
        RailDestination.FastFlags,
        RailDestination.About
    )

    NavigationRail {
        destinations.forEach {
            NavigationRailItem(
                selected = it.route == currentRoute,
                onClick = { onNavigate(it.route) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.label
                    )
                },
                label = { Text(it.label) }
            )
        }
    }
}