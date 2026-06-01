package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MatchInProgressState(
    val player1Name: String = "Player 1",
    val player2Name: String = "Player 2",
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val player1Sets: Int = 0,
    val player2Sets: Int = 0,
    val isMatchFinished: Boolean = false,
    val winnerName: String? = null
)

class MatchInProgressViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MatchInProgressState())
    val uiState: StateFlow<MatchInProgressState> = _uiState.asStateFlow()

    fun setupMatch(p1Name: String, p2Name: String) {
        _uiState.update { it.copy(player1Name = p1Name, player2Name = p2Name) }
    }

    fun incrementPlayer1Score() {
        if (_uiState.value.isMatchFinished) return
        
        val newScore = _uiState.value.player1Score + 1
        _uiState.update { it.copy(player1Score = newScore) }
        checkSetEnd()
    }

    fun decrementPlayer1Score() {
        if (_uiState.value.isMatchFinished) return
        if (_uiState.value.player1Score > 0) {
            _uiState.update { it.copy(player1Score = it.player1Score - 1) }
        }
    }

    fun incrementPlayer2Score() {
        if (_uiState.value.isMatchFinished) return
        
        val newScore = _uiState.value.player2Score + 1
        _uiState.update { it.copy(player2Score = newScore) }
        checkSetEnd()
    }

    fun decrementPlayer2Score() {
        if (_uiState.value.isMatchFinished) return
        if (_uiState.value.player2Score > 0) {
            _uiState.update { it.copy(player2Score = it.player2Score - 1) }
        }
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
            _uiState.update { it.copy(player1Sets = newSets, player1Score = 0, player2Score = 0) }
            if (newSets >= 3) { // Best of 5, so 3 sets to win
                finishMatch(_uiState.value.player1Name)
            }
        } else {
            val newSets = _uiState.value.player2Sets + 1
            _uiState.update { it.copy(player2Sets = newSets, player1Score = 0, player2Score = 0) }
            if (newSets >= 3) {
                finishMatch(_uiState.value.player2Name)
            }
        }
    }

    private fun finishMatch(winnerName: String) {
        _uiState.update { it.copy(isMatchFinished = true, winnerName = winnerName) }
    }
    
    fun resetMatch() {
        _uiState.update { 
            it.copy(
                player1Score = 0, 
                player2Score = 0, 
                player1Sets = 0, 
                player2Sets = 0, 
                isMatchFinished = false, 
                winnerName = null
            ) 
        }
    }
}
