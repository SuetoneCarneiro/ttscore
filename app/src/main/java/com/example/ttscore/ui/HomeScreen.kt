package com.example.ttscore.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttscore.ui.components.buttons.BotaoLogin
import com.example.ttscore.ui.components.buttons.BotaoRegistrar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showRegisterModal by remember { mutableStateOf(false) }

    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)

    Scaffold(
        containerColor = darkNavy
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SportsTennis,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
            
            Text(
                text = "TTScore",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha", color = Color.White.copy(alpha = 0.7f)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
            BotaoLogin(
                username = username,
                pass = password,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                onSuccess = {
                    Toast.makeText(context, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                },
                onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = { showRegisterModal = true }) {
                Text("Não tem uma conta? Registrar-se", color = Color.White)
            }
        }
    }

    if (showRegisterModal) {
        RegisterModal(
            onDismiss = { showRegisterModal = false },
            onRegistrationSuccess = {
                showRegisterModal = false
                Toast.makeText(context, "Cadastro realizado com sucesso! Agora faça login.", Toast.LENGTH_LONG).show()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterModal(
    onDismiss: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val darkNavy = Color(0xFF001F3F)
    val p2Red = Color(0xFFB71C1C)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = darkNavy,
        title = { Text("Registrar Jogador", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nome de Usuário", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = p2Red,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = p2Red,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha", color = Color.White.copy(alpha = 0.7f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = p2Red,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            BotaoRegistrar(
                username = username,
                email = email,
                pass = password,
                onSuccess = { onRegistrationSuccess() },
                onError = { error -> 
                    Toast.makeText(context, "Erro no cadastro: $error", Toast.LENGTH_LONG).show() 
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}
