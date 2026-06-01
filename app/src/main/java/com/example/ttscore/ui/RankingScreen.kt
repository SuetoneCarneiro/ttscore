package com.example.ttscore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource
import kotlinx.coroutines.flow.first
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.EntryPoint

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SessionManagerEntryPoint {
    fun sessionManager(): SessionManager
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    onBack: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val rankingState by viewModel.rankingState.collectAsState()

    LaunchedEffect(Unit) {
        // Busca o SessionManager através do EntryPoint
        val sessionManager = EntryPointAccessors.fromApplication(
            context.applicationContext,
            SessionManagerEntryPoint::class.java
        ).sessionManager()
        
        // Carrega o token salvo
        val token = sessionManager.token.first()
        if (token != null) {
            viewModel.buscarRanking("Bearer $token")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranking Global") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = rankingState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Success -> {
                    val rankingList = state.data ?: emptyList()
                    if (rankingList.isEmpty()) {
                        Text(
                            text = "Nenhum jogador encontrado",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        RankingList(rankingList)
                    }
                }
                is Resource.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Erro ao carregar ranking", color = Color.Red)
                        Text(text = state.message ?: "", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { 
                            // Tentar novamente
                            onBack() 
                        }) {
                            Text("Voltar e Logar novamente")
                        }
                    }
                }
                null -> {
                    Text(
                        text = "Carregando Ranking...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun RankingList(usuarios: List<Usuario>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(usuarios) { index, usuario ->
            RankingItem(rank = index + 1, usuario = usuario)
        }
    }
}

@Composable
fun RankingItem(rank: Int, usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.width(45.dp)
                )
                Column {
                    Text(
                        text = usuario.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${usuario.wins} Vitórias • ${usuario.losses} Derrotas",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                val totalGames = usuario.wins + usuario.losses
                val winRate = if (totalGames > 0) (usuario.wins.toFloat() / totalGames * 100).toInt() else 0
                
                Text(
                    text = "$winRate%",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = if (winRate >= 50) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
                Text(text = "Win Rate", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
