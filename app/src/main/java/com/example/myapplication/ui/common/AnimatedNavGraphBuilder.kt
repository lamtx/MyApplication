package com.example.myapplication.ui.common

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedComposable(
    path: String,
    args: (NavArgBuilder.() -> Unit)? = null,
    deepLinks: List<NavDeepLink> = emptyList(),
    transitions: RouteTransitions = RouteTransitions.FadeUpwards,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    val routeBuilder = RouteBuilder(path, args)
    composable(
        route = routeBuilder.buildRoute(),
        arguments = routeBuilder.buildArgs(),
        deepLinks = deepLinks,
        content = content,
        enterTransition = transitions.enterTransition,
        exitTransition = transitions.exitTransition,
        popEnterTransition = transitions.popEnterTransition,
        popExitTransition = transitions.popExitTransition,
    )
}

class RouteTransitions(
    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
) {
    fun copy(
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = this.enterTransition,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = this.exitTransition,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = this.popEnterTransition,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = this.popExitTransition,
    ): RouteTransitions {
        return RouteTransitions(
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        )
    }

    companion object {
        val FadeUpwards = RouteTransitions(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it / 4 }
                ) + fadeIn()
            },
            exitTransition = {
                fadeOut()
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it / 4 }
                ) + fadeOut()
            },
        )
        val TopLevel = RouteTransitions(
            enterTransition = {
                scaleIn(
                    initialScale = 1.1f
                ) + fadeIn()
            },
            exitTransition = {
                fadeOut()
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                scaleOut(
                    targetScale = 1.1f
                ) + fadeOut()
            },
        )
    }
}
