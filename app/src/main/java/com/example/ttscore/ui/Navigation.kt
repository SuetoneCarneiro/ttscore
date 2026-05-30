package com.example.ttscore.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NovaPartida : Screen("nova_partida")
    object CadastrarJogadores : Screen("cadastrar_jogadores")
    object ListarJogadores : Screen("listar_jogadores")
    object Ranking : Screen("ranking")
    object FirebaseTest : Screen("firebase_test")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToNovaPartida = { navController.navigate(Screen.NovaPartida.route) },
                onNavigateToCadastrarJogadores = { navController.navigate(Screen.CadastrarJogadores.route) },
                onNavigateToListarJogadores = { navController.navigate(Screen.ListarJogadores.route) },
                onNavigateToRanking = { navController.navigate(Screen.Ranking.route) },
                onNavigateToFirebaseTest = { navController.navigate(Screen.FirebaseTest.route) }
            )
        }
        composable(Screen.NovaPartida.route) {
            NovaPartidaScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.CadastrarJogadores.route) {
            CadastrarJogadoresScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ListarJogadores.route) {
            ListarJogadoresScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Ranking.route) {
            RankingScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.FirebaseTest.route) {
            FirebaseTestScreen(onBack = { navController.popBackStack() })
        }
    }
}
