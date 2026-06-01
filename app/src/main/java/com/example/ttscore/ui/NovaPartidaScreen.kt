package com.example.ttscore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaPartidaScreen(
    onBack: () -> Unit,
    onStartMatch: (String, String, Boolean) -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    var showFriendSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()

    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)
    val p2Red = Color(0xFFB71C1C)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (showFriendSearch) "Buscar Amigo" else "Nova Partida",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showFriendSearch) {
                            showFriendSearch = false
                            viewModel.resetStates()
                            searchQuery = ""
                        } else {
                            onBack()
                        }
                    }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (!showFriendSearch) {
                // Modo de Seleção Inicial
                Button(
                    onClick = { showFriendSearch = true },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = p1Blue),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "JOGAR COM AMIGO (RANKED)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onStartMatch("Você", "Convidado", true) },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.5f)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "JOGAR CASUALMENTE (OFFLINE)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Busca de Amigos
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        viewModel.pesquisarUsuarios(it)
                    },
                    label = { Text("Nome do usuário", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = p1Blue,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = p1Blue,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                when (val state = searchState) {
                    is Resource.Loading -> CircularProgressIndicator(color = Color.White)
                    is Resource.Success -> {
                        val users = state.data ?: emptyList()
                        if (users.isEmpty() && searchQuery.isNotBlank()) {
                            Text("Usuário não encontrado", color = p2Red, fontWeight = FontWeight.Bold)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(users) { user ->
                                    UserListItem(user = user) {
                                        onStartMatch("Você", user.username, false)
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text("Erro: ${state.message}", color = p2Red, fontWeight = FontWeight.Bold)
                    }
                    null -> {
                        if (searchQuery.isBlank()) {
                            Text("Digite o nome para buscar", color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: Usuario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = user.username, 
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Vitórias: ${user.wins} | Derrotas: ${user.losses}", 
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
