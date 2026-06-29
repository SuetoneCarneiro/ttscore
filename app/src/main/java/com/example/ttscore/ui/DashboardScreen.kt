package com.example.ttscore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToNovaPartida: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToHistory: (String, String) -> Unit,
    onLogout: () -> Unit,
    viewModel: UserViewModel = koinViewModel()
) {
    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)
    val p2Red = Color(0xFFB71C1C)
    val scrollState = rememberScrollState()
    
    val myProfileState by viewModel.myProfileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarMeuPerfil()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SportsTennis, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TTScore", fontWeight = FontWeight.Black)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Sair", tint = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // card de perfil resumido
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccountCircle, 
                        contentDescription = null, 
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        when (val state = myProfileState) {
                            is Resource.Success -> {
                                val user = state.data!!
                                Text(user.username, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Vitórias: ${user.wins}", color = Color(0xFF4CAF50), fontSize = 14.sp)
                                    Text("Derrotas: ${user.losses}", color = Color(0xFFF44336), fontSize = 14.sp)
                                }
                            }
                            is Resource.Loading -> {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = p1Blue)
                            }
                            else -> {
                                Text("Olá!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Perfil", tint = Color.White.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            DashboardButton(
                text = "NOVA PARTIDA",
                icon = Icons.Default.AddCircle,
                color = p1Blue,
                onClick = onNavigateToNovaPartida
            )

            DashboardButton(
                text = "RANKING GLOBAL",
                icon = Icons.Default.Leaderboard,
                color = p2Red,
                onClick = onNavigateToRanking
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))


            Text(
                "HISTÓRICO E ANÁLISE",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            DashboardButton(
                text = "MEU HISTÓRICO",
                icon = Icons.Default.History,
                color = Color.White.copy(alpha = 0.05f),
                onClick = { onNavigateToHistory("me", "list") }
            )


            var searchQuery by remember { mutableStateOf("") }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar jogador para ver Versus", color = Color.White.copy(alpha = 0.4f)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { 
                        if (searchQuery.isNotBlank()) onNavigateToHistory(searchQuery.trim(), "versus") 
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = p1Blue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
