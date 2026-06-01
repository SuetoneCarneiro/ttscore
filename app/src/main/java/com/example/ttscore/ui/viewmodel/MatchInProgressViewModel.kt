package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.repository.MatchRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchInProgressState(
    val player1Name: String = "Jogador 1",
    val player2Name: String = "Jogador 2",
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val player1Sets: Int = 0,
    val player2Sets: Int = 0,
    val isMatchFinished: Boolean = false,
    val winnerName: String? = null,
    val isCasual: Boolean = true,
    val saveResultStatus: Resource<Unit>? = null
)

@HiltViewModel
class MatchInProgressViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchInProgressState())
    val uiState: StateFlow<MatchInProgressState> = _uiState.asStateFlow()

    fun setupMatch(p1Name: String, p2Name: String, isCasual: Boolean) {
        _uiState.update { it.copy(
            player1Name = p1Name,
            player2Name = p2Name,
            isCasual = isCasual,
            player1Score = 0,
            player2Score = 0,
            player1Sets = 0,
            player2Sets = 0,
            isMatchFinished = false,
            winnerName = null,
            saveResultStatus = null
        ) }
    }

    fun incrementPlayer1Score() {
        if (_uiState.value.isMatchFinished) return
        val newScore = _uiState.value.player1Score + 1
        _uiState.update { it.copy(player1Score = newScore) }
        checkSetEnd()
    }

    fun decrementPlayer1Score() {
        if (_uiState.value.isMatchFinished || _uiState.value.player1Score <= 0) return
        _uiState.update { it.copy(player1Score = it.player1Score - 1) }
    }

    fun incrementPlayer2Score() {
        if (_uiState.value.isMatchFinished) return
        val newScore = _uiState.value.player2Score + 1
        _uiState.update { it.copy(player2Score = newScore) }
        checkSetEnd()
    }

    fun decrementPlayer2Score() {
        if (_uiState.value.isMatchFinished || _uiState.value.player2Score <= 0) return
        _uiState.update { it.copy(player2Score = it.player2Score - 1) }
    }

    private fun checkSetEnd() {
        val p1Score = _uiState.value.player1Score
        val p2Score = _uiState.value.player2Score

        if (p1Score >= 11 && (p1Score - p2Score) >= 2) {
            finishSet(1)
        } else if (p2Score >= 11 && (p2Score - p1Score) >= 2) {
            finishSet(2)
        }
    }

    private fun finishSet(winner: Int) {
        if (winner == 1) {
            val newSets = _uiState.value.player1Sets + 1
            if (newSets >= 3) {
                _uiState.update { it.copy(player1Sets = newSets, isMatchFinished = true, winnerName = it.player1Name) }
                handleMatchEnd()
            } else {
                _uiState.update { it.copy(player1Sets = newSets, player1Score = 0, player2Score = 0) }
            }
        } else {
            val newSets = _uiState.value.player2Sets + 1
            if (newSets >= 3) {
                _uiState.update { it.copy(player2Sets = newSets, isMatchFinished = true, winnerName = it.player2Name) }
                handleMatchEnd()
            } else {
                _uiState.update { it.copy(player2Sets = newSets, player1Score = 0, player2Score = 0) }
            }
        }
    }

    private fun handleMatchEnd() {
        val state = _uiState.value
        if (!state.isCasual) {
            saveToApi(state)
        }
    }

    private fun saveToApi(state: MatchInProgressState) {
        viewModelScope.launch {
            _uiState.update { it.copy(saveResultStatus = Resource.Loading()) }
            val token = sessionManager.token.first()
            if (token != null) {
                val result = matchRepository.createMatch(
                    token = token,
                    request = MatchRequest(
                        opponentUsername = state.player2Name,
                        player1Score = state.player1Sets,
                        player2Score = state.player2Sets
                    )
                )
                
                result.onSuccess {
                    _uiState.update { it.copy(saveResultStatus = Resource.Success(Unit)) }
                }
                result.onFailure { throwable ->
                    _uiState.update { it.copy(saveResultStatus = Resource.Error(throwable.message ?: "Erro ao salvar resultado")) }
                }
            } else {
                _uiState.update { it.copy(saveResultStatus = Resource.Error("Token não encontrado")) }
            }
        }
    }
}
