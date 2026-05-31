package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.ui.viewmodel.LoginViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoLogin(
    username: String,
    pass: String,
    modifier: Modifier = Modifier,
    onSuccess: (AuthResponse) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.loginState.collectAsState()

    // Usando uma variável local para permitir smart cast
    val currentState = state
    if (currentState is Resource.Success) {
        currentState.data?.let {
            onSuccess(it)
            viewModel.resetState()
        }
    } else if (currentState is Resource.Error) {
        onError(currentState.message ?: "Erro ao fazer login")
        viewModel.resetState()
    }

    LoadingButton(
        text = "Entrar",
        onClick = { viewModel.login(username, pass) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = username.isNotBlank() && pass.isNotBlank()
    )
}
