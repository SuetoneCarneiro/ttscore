package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoAtualizarPerfil(
    token: String,
    username: String?,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.userState.collectAsState()

    if (state is Resource.Success) {
        onSuccess()
    } else if (state is Resource.Error) {
        onError((state as Resource.Error).message ?: "Erro ao atualizar perfil")
    }

    LoadingButton(
        text = "Salvar Alterações",
        onClick = { viewModel.atualizarPerfil(token, username, avatarUrl) },
        isLoading = state is Resource.Loading,
        modifier = modifier
    )
}
