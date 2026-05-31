package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.ui.viewmodel.FriendshipViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoAceitarAmizade(
    token: String,
    friendshipId: String,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: FriendshipViewModel = hiltViewModel()
) {
    val state by viewModel.friendshipState.collectAsState()

    if (state is Resource.Success) {
        onSuccess()
        viewModel.resetStates()
    } else if (state is Resource.Error) {
        onError((state as Resource.Error).message ?: "Erro ao aceitar pedido")
        viewModel.resetStates()
    }

    LoadingButton(
        text = "Aceitar Pedido",
        onClick = { viewModel.aceitarPedido(token, friendshipId) },
        isLoading = state is Resource.Loading,
        modifier = modifier
    )
}
