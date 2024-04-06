package com.manish.teachmintassignment.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manish.teachmintassignment.presentation.Routes.HOME_SCREEN
import com.manish.teachmintassignment.presentation.Routes.REPO_DETAILS_SCREEN
import com.manish.teachmintassignment.presentation.screens.homeScreen.HomeScreen
import com.manish.teachmintassignment.presentation.screens.repoDetails.RepoDetailsScreen

object Routes {
    const val HOME_SCREEN = "homeScreen"
    const val REPO_DETAILS_SCREEN = "repoDetailsScreen"
}

@Composable
fun TeachMintNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = HOME_SCREEN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(HOME_SCREEN) {
            HomeScreen(navController)
        }
        composable(REPO_DETAILS_SCREEN) {
            RepoDetailsScreen(navController)
        }
    }
}