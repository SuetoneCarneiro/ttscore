package com.example.ttscore.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.domain.model.Match
import com.example.ttscore.ui.viewmodel.MatchViewModel
import com.example.ttscore.util.Resource

@Composable
fun BotaoCriarPartida(
    opponentUsername: String,
    player1Score: Int,
    player2Score: Int,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onError: (String) -> Unit = {},
    viewModel: MatchViewModel = hiltViewModel()
) {
    val state by viewModel.matchState.collectAsState()

    val currentState = state
    if (currentState is Resource.Success) {
        onSuccess()
        viewModel.resetState()
    } else if (currentState is Resource.Error) {
        onError(currentState.message ?: "Erro ao registrar partida")
        viewModel.resetState()
    }

    LoadingButton(
        text = "Registrar Partida",
        onClick = { viewModel.criarPartida(opponentUsername, player1Score, player2Score) },
        isLoading = state is Resource.Loading,
        modifier = modifier,
        enabled = opponentUsername.isNotBlank()
    )
}
