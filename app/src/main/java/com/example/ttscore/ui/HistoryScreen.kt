package com.example.ttscore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttscore.domain.model.Match
import com.example.ttscore.ui.viewmodel.MatchViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    username: String,
    mode: String,
    onBack: () -> Unit,
    viewModel: MatchViewModel = koinViewModel()
) {
    val uiState by viewModel.matchListUiState.collectAsState()
    
    val darkNavy = Color(0xFF001F3F)
    val p2Red = Color(0xFFB71C1C)

    LaunchedEffect(Unit) {
        when (mode) {
            "versus" -> viewModel.buscarHeadToHead(username)
            "user" -> viewModel.buscarPartidasPorUsername(username)
            else -> viewModel.buscarMinhasPartidas()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when(mode) {
                            "versus" -> "Versus: $username"
                            "user" -> "Partidas de $username"
                            else -> "Meu Histórico"
                        },
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkNavy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = darkNavy
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "Erro ao carregar histórico",
                    color = p2Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            } else {
                if (uiState.matches.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(64.dp))
                        Text("Nenhuma partida encontrada", color = Color.White.copy(alpha = 0.5f))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.matches) { match ->
                            MatchItem(match)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchItem(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(match.player1.username, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(match.player1Score.toString(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
                
                Text("X", color = Color.White.copy(alpha = 0.5f), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(match.player2.username, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(match.player2Score.toString(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = match.playedAt.take(10).split("-").reversed().joinToString("/"),
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
