package com.example.ttscore.ui.components.buttons

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.ui.viewmodel.CadastroViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun BotaoRegistrar(
    username: String,
    email: String,
    pass: String,
    modifier: Modifier = Modifier,
    onSuccess: (AuthResponse) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: CadastroViewModel = koinViewModel()
) {
    val state by viewModel.cadastroState.collectAsState()

    LaunchedEffect(state) {
        when (val currentState = state) {
            is Resource.Success -> {
                currentState.data?.let {
                    onSuccess(it)
                    viewModel.resetState()
                }
            }
            is Resource.Error -> {
                onError(currentState.message ?: "Erro desconhecido")
                viewModel.resetState()
            }
            else -> {}
        }
    }

    LoadingButton(
        text = "Registrar",
        onClick = { viewModel.cadastrar(username.trim(), email.trim(), pass.trim()) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = username.isNotBlank() && email.isNotBlank() && pass.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
    )
}
