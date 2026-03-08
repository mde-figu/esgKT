package com.ecotracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecotracker.ui.screens.AirQualityScreen
import com.ecotracker.ui.screens.DashboardScreen
import com.ecotracker.ui.screens.EcoTipsScreen
import com.ecotracker.ui.screens.FoodImpactScreen
import com.ecotracker.ui.screens.TransportScreen
import com.ecotracker.ui.screens.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val DASHBOARD = "dashboard"
    const val TRANSPORT = "transport"
    const val FOOD = "food"
    const val AIR_QUALITY = "air_quality"
    const val ECO_TIPS = "eco_tips"
}

@Composable
fun EcoTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToTransport = { navController.navigate(Routes.TRANSPORT) },
                onNavigateToFood = { navController.navigate(Routes.FOOD) },
                onNavigateToAirQuality = { navController.navigate(Routes.AIR_QUALITY) },
                onNavigateToTips = { navController.navigate(Routes.ECO_TIPS) }
            )
        }
        composable(Routes.TRANSPORT) {
            TransportScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.FOOD) {
            FoodImpactScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.AIR_QUALITY) {
            AirQualityScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ECO_TIPS) {
            EcoTipsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
