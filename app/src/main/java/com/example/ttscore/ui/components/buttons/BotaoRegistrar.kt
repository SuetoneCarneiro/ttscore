package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.ui.viewmodel.CadastroViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoRegistrar(
    username: String,
    email: String,
    pass: String,
    modifier: Modifier = Modifier,
    onSuccess: (AuthResponse) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: CadastroViewModel = hiltViewModel()
) {
    val state by viewModel.cadastroState.collectAsState()

    val currentState = state
    if (currentState is Resource.Success) {
        currentState.data?.let {
            onSuccess(it)
            viewModel.resetState()
        }
    } else if (currentState is Resource.Error) {
        onError(currentState.message ?: "Erro desconhecido")
        viewModel.resetState()
    }

    LoadingButton(
        text = "Registrar",
        onClick = { viewModel.cadastrar(username, email, pass) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = username.isNotBlank() && email.isNotBlank() && pass.isNotBlank()
    )
}
