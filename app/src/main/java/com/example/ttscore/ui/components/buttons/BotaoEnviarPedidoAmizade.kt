package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.domain.model.Friendship
import com.example.ttscore.ui.viewmodel.FriendshipViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoEnviarPedidoAmizade(
    token: String,
    addresseeId: String,
    forceAccept: Boolean = false,
    label: String = "Adicionar Amigo",
    modifier: Modifier = Modifier,
    onSuccess: (Friendship) -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: FriendshipViewModel = hiltViewModel()
) {
    val state by viewModel.friendshipState.collectAsState()

    if (state is Resource.Success) {
        val friendship = (state as Resource.Success<Friendship>).data
        if (friendship != null) onSuccess(friendship)
        viewModel.resetStates()
    } else if (state is Resource.Error) {
        onError((state as Resource.Error).message ?: "Erro ao enviar pedido")
        viewModel.resetStates()
    }

    LoadingButton(
        text = label,
        onClick = { viewModel.enviarPedido(token, addresseeId, forceAccept) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = addresseeId.isNotBlank()
    )
}
