package com.agcoding.oral.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agcoding.oral.screen.CaptureScreen
import com.agcoding.oral.screen.EndSessionScreen
import com.agcoding.oral.screen.HomeScreen
import com.agcoding.oral.screen.SearchScreen
import com.agcoding.oral.screen.SessionDetailScreen
import com.agcoding.oral.viewmodels.CaptureViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedCaptureVm  : CaptureViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(
            Screens.Home.route) {
            HomeScreen(
                onStartCapture = { navController.navigate(Screens.Capture.route) },
                onSearch = { navController.navigate(Screens.Search.route) }
            )
        }
        composable(
            Screens.Capture.route) {
            CaptureScreen(
                navController = navController,
                viewModel = sharedCaptureVm
            )
        }
        composable(
            Screens.EndSession.route) { EndSessionScreen(navController, sharedCaptureVm)}
        composable(
            Screens.Search.route) { SearchScreen(navController)  }
        composable(
            Screens.SessionDetail.route) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId").orEmpty()
            SessionDetailScreen(sessionId)
        }
    }
}