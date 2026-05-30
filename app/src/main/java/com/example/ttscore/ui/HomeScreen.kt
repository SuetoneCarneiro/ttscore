package com.example.ttscore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNovaPartida: () -> Unit,
    onNavigateToCadastrarJogadores: () -> Unit,
    onNavigateToListarJogadores: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToFirebaseTest: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TTScore") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simplified buttons as requested
            MenuButton(text = "Iniciar nova partida", onClick = onNavigateToNovaPartida)
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Cadastrar Jogadores", onClick = onNavigateToCadastrarJogadores)
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Listar Jogadores", onClick = onNavigateToListarJogadores)
            Spacer(modifier = Modifier.height(16.dp))
            MenuButton(text = "Visualizar Ranking", onClick = onNavigateToRanking)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateToFirebaseTest) {
                Text(text = "Testar Conexão com Firebase")
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text.uppercase())
    }
}
