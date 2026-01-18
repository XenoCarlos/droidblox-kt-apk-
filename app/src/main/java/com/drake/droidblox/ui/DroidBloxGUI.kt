package com.drake.droidblox.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.drake.droidblox.ui.view.navigation.AppNavigationRail
import com.drake.droidblox.ui.view.views.AboutScreen
import com.drake.droidblox.ui.view.views.FFlagsScreen
import com.drake.droidblox.ui.view.views.IntegrationsScreen
import com.drake.droidblox.ui.view.views.LoginToDiscordScreen
//import com.drake.droidblox.ui.view.PlayLogsScreen
import com.drake.droidblox.ui.view.navigation.Routes
import com.drake.droidblox.ui.view.navigation.animatedComposable

@Composable
fun DroidBloxGUI() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(modifier = Modifier.fillMaxSize()) {
        AppNavigationRail(
            currentRoute = currentRoute ?: Routes.INTEGRATIONS,
        ) {
            navController.navigate(route = it) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
        NavHost(
            navController = navController,
            startDestination = Routes.INTEGRATIONS
        ) {
            animatedComposable(Routes.INTEGRATIONS) {
                IntegrationsScreen(
                    navController = navController
                )
            }
            animatedComposable(Routes.FFLAGS) {
                FFlagsScreen(
                    navController = navController
                )
            }
            animatedComposable(Routes.ABOUT) {
                AboutScreen(
                    navController = navController
                )
            }
        }
    }
}