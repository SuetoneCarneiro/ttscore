package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ttscore.ui.viewmodel.FriendshipViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun BotaoRemoverAmizade(
    token: String,
    friendshipId: String,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: FriendshipViewModel = koinViewModel()
) {
    val actionState by viewModel.actionState.collectAsState()

    if (actionState is Resource.Success) {
        onSuccess()
        viewModel.resetStates()
    } else if (actionState is Resource.Error) {
        onError((actionState as Resource.Error).message ?: "Erro ao remover amizade")
        viewModel.resetStates()
    }

    LoadingButton(
        text = "Remover Amigo",
        onClick = { viewModel.removerAmizade(token, friendshipId) },
        isLoading = actionState is Resource.Loading,
        modifier = modifier
    )
}
