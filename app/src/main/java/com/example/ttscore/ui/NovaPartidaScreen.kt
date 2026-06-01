package com.example.ttscore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaPartidaScreen(onBack: () -> Unit, onStartMatch: (String, String) -> Unit) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Nova Partida") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = player1Name,
                onValueChange = { player1Name = it },
                label = { Text("Nome do Jogador 1") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = player2Name,
                onValueChange = { player2Name = it },
                label = { Text("Nome do Jogador 2") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { 
                    if (player1Name.isNotBlank() && player2Name.isNotBlank()) {
                        onStartMatch(player1Name, player2Name)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = player1Name.isNotBlank() && player2Name.isNotBlank()
            ) {
                Text("INICIAR PARTIDA")
            }
        }
    }
}
