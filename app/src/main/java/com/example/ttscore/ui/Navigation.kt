package com.example.ttscore.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object NovaPartida : Screen("nova_partida")
    object Ranking : Screen("ranking")
    object Profile : Screen("profile")
    object History : Screen("history/{username}/{mode}") {
        fun createRoute(username: String, mode: String) = "history/$username/$mode"
    }
    object Partida : Screen("partida/{p1}/{p2}/{isCasual}") {
        fun createRoute(p1: String, p2: String, isCasual: Boolean) = "partida/$p1/$p2/$isCasual"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            HomeScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToNovaPartida = { navController.navigate(Screen.NovaPartida.route) },
                onNavigateToRanking = { navController.navigate(Screen.Ranking.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToHistory = { username, mode ->
                    navController.navigate(Screen.History.createRoute(username, mode)) 
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.NovaPartida.route) {
            NovaPartidaScreen(
                onBack = { navController.popBackStack() },
                onStartMatch = { p1, p2, isCasual ->
                    navController.navigate(Screen.Partida.createRoute(p1, p2, isCasual))
                }
            )
        }
        composable(
            route = Screen.Partida.route,
            arguments = listOf(
                navArgument("p1") { type = NavType.StringType },
                navArgument("p2") { type = NavType.StringType },
                navArgument("isCasual") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val p1 = backStackEntry.arguments?.getString("p1") ?: "Jogador 1"
            val p2 = backStackEntry.arguments?.getString("p2") ?: "Jogador 2"
            val isCasual = backStackEntry.arguments?.getBoolean("isCasual") ?: true
            
            PartidaScreen(
                player1Name = p1,
                player2Name = p2,
                isCasual = isCasual,
                onFinish = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Ranking.route) {
            RankingScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHistory = { username, mode ->
                    navController.navigate(Screen.History.createRoute(username, mode))
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.History.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("mode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "me"
            val mode = backStackEntry.arguments?.getString("mode") ?: "list"
            HistoryScreen(
                username = username,
                mode = mode,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
