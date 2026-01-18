package com.drake.droidblox.ui.view.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

//fun NavGraphBuilder.animatedComposable(
//    route: String,
//    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
//) = composable(
//    route = route,
//    enterTransition = {
//        fadeIn(animationSpec = tween(220, delayMillis = 90)) +
//                scaleIn(
//                    initialScale = 0.92f,
//                    animationSpec = tween(220, delayMillis = 90)
//                )
//    },
//    popEnterTransition = {
//        fadeIn(animationSpec = tween(220, delayMillis = 90)) +
//                scaleIn(
//                    initialScale = 0.92f,
//                    animationSpec = tween(220, delayMillis = 90)
//                )
//    },
//    content = content
//)
private val animationSpec = tween<Float>(durationMillis = 400)
fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    enterTransition = {
        fadeIn(animationSpec = tween(300)) +
        scaleIn(initialScale = 0.85f, animationSpec = tween(400))
    },
    content = content
)