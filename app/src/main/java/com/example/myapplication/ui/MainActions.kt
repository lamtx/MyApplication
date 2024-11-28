package com.example.myapplication.ui

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.myapplication.ui.common.RouteParameterBuilder
import com.example.myapplication.ui.common.buildRoute


@JvmInline
value class MainActions(private val navController: NavController) {
    fun back(): Boolean {
        return navController.popBackStack()
    }

    fun openArticle(index: Long) {
        navigate(MainDestinations.ARTICLES) {
            MainDestinations.KEY_INDEX set index
        }
    }

    private fun navigate(
        path: String,
        options: NavOptions? = null,
        builder: (RouteParameterBuilder.() -> Unit)? = null,
    ) {
        navController.navigate(buildRoute(path, builder), options)
    }
}