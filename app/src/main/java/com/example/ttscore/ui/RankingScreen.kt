package com.example.ttscore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    onBack: () -> Unit,
    onNavigateToHistory: (String, String) -> Unit,
    viewModel: UserViewModel = koinViewModel()
) {
    val uiState by viewModel.rankingUiState.collectAsState()
    var selectedUser by remember { mutableStateOf<Usuario?>(null) }

    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)

    LaunchedEffect(Unit) {
        viewModel.carregarRanking()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranking Global", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Voltar", 
                            tint = Color.White
                        )
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
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Erro ao carregar ranking", color = Color(0xFFB71C1C), fontWeight = FontWeight.Bold)
                    Text(text = uiState.errorMessage ?: "", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = p1Blue)) {
                        Text("Voltar", color = Color.White)
                    }
                }
            } else {
                if (uiState.ranking.isEmpty()) {
                    Text(
                        text = "Nenhum jogador encontrado",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    RankingList(uiState.ranking) { user -> selectedUser = user }
                }
            }
        }
    }

    if (selectedUser != null) {
        AlertDialog(
            onDismissRequest = { selectedUser = null },
            containerColor = darkNavy,
            title = { Text(selectedUser!!.username, color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            onNavigateToHistory(selectedUser!!.username, "user")
                            selectedUser = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Histórico do Jogador", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            onNavigateToHistory(selectedUser!!.username, "versus")
                            selectedUser = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = p1Blue)
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Retrospecto (Versus)", color = Color.White)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUser = null }) {
                    Text("FECHAR", color = Color.White.copy(alpha = 0.6f))
                }
            }
        )
    }
}

@Composable
fun RankingList(usuarios: List<Usuario>, onUserClick: (Usuario) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(usuarios) { index, usuario ->
            RankingItem(rank = index + 1, usuario = usuario, onClick = { onUserClick(usuario) })
        }
    }
}

@Composable
fun RankingItem(rank: Int, usuario: Usuario, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#$rank",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = when(rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> Color.White
                    },
                    modifier = Modifier.width(45.dp)
                )
                Column {
                    Text(text = usuario.username, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Text(text = "${usuario.wins} V • ${usuario.losses} D", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                val total = usuario.wins + usuario.losses
                val rate = if (total > 0) (usuario.wins.toFloat() / total * 100).toInt() else 0
                Text(text = "$rate%", fontWeight = FontWeight.Black, fontSize = 18.sp, color = if (rate >= 50) Color(0xFF4CAF50) else Color(0xFFF44336))
                Text(text = "Win Rate", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}
