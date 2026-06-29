package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoAtualizarPerfil(
    username: String?,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: UserViewModel = koinViewModel()
) {
    val state by viewModel.userState.collectAsState()

    if (state is Resource.Success) {
        onSuccess()
    } else if (state is Resource.Error) {
        onError((state as Resource.Error).message ?: "Erro ao atualizar perfil")
    }

    LoadingButton(
        text = "Salvar Alterações",
        onClick = { viewModel.atualizarPerfil(username, avatarUrl) },
        isLoading = state is Resource.Loading,
        modifier = modifier
    )
}
