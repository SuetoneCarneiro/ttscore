package com.example.ttscore.ui.components.buttons

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.ui.viewmodel.LoginViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun BotaoLogin(
    username: String,
    pass: String,
    modifier: Modifier = Modifier,
    onSuccess: (AuthResponse) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.loginState.collectAsState()

    LaunchedEffect(state) {
        when (val currentState = state) {
            is Resource.Success -> {
                currentState.data?.let {
                    onSuccess(it)
                    viewModel.resetState()
                }
            }
            is Resource.Error -> {
                onError(currentState.message ?: "Erro ao fazer login")
                viewModel.resetState()
            }
            else -> {}
        }
    }

    LoadingButton(
        text = "Entrar",
        onClick = { viewModel.login(username.trim(), pass.trim()) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = username.isNotBlank() && pass.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
    )
}
