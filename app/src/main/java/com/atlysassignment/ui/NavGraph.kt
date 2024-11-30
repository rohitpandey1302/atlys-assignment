package com.atlysassignment.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atlysassignment.compose.HomePageScreen
import com.atlysassignment.ui.viewmodels.HomePageViewModel

@Composable
fun NavGraph(viewModel: HomePageViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "movieList") {
        composable("movieList") {
            HomePageScreen(viewModel = viewModel) { movie ->
                Log.e("Rohit", "Pandey movie clicked :: $movie")
            }
        }
    }
}