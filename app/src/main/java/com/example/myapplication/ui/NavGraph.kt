package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.articledetail.ArticleDetail
import com.example.myapplication.ui.articleslist.ArticlesList
import com.example.myapplication.ui.common.animatedComposable


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = MainDestinations.ARTICLES,
) {
    val navController = rememberNavController()
    val actions = remember { MainActions(navController) }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        animatedComposable(MainDestinations.ARTICLES) {
            ArticlesList(
                onOpenArticle = actions::openArticle
            )
        }
        animatedComposable(path = MainDestinations.ARTICLES,
            args = {
                long(MainDestinations.KEY_INDEX)
            }
        ) {
            ArticleDetail(
                onBack = actions::back,
            )
        }
    }
}