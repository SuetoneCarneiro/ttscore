package com.example.ttscore.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()
    val myProfileState by viewModel.myProfileState.collectAsState()

    var username by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)

    LaunchedEffect(Unit) {
        viewModel.carregarMeuPerfil()
    }

    LaunchedEffect(myProfileState) {
        if (myProfileState is Resource.Success) {
            val user = (myProfileState as Resource.Success).data
            if (username.isEmpty()) username = user?.username ?: ""
            if (avatarUrl.isEmpty()) avatarUrl = user?.avatarUrl ?: ""
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is Resource.Success -> Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
            is Resource.Error -> Toast.makeText(context, (userState as Resource.Error).message, Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nome de usuário", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = p1Blue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = avatarUrl,
                onValueChange = { avatarUrl = it },
                label = { Text("URL do Avatar", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = p1Blue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.atualizarPerfil(username.trim(), avatarUrl.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = p1Blue),
                enabled = userState !is Resource.Loading
            ) {
                if (userState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("ATUALIZAR PERFIL", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
