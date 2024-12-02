package com.atlysassignment.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atlysassignment.compose.DetailPageScreen
import com.atlysassignment.compose.HomePageScreen
import com.atlysassignment.ui.viewmodels.MainActivityViewModel

@Composable
fun NavGraph(viewModel: MainActivityViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "movieList") {
        composable("movieList") {
            HomePageScreen(viewModel = viewModel) { movieId ->
                navController.navigate("movieDetail/$movieId")
            }
        }

        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            DetailPageScreen(
                movieId = movieId,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}