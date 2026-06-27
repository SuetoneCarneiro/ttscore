package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.repository.MatchRepository
import com.example.ttscore.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatchViewModel(
    private val repository: MatchRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _matchListState = MutableStateFlow<Resource<List<Match>>?>(null)
    val matchListState = _matchListState.asStateFlow()

    private val _matchState = MutableStateFlow<Resource<Match>?>(null)
    val matchState = _matchState.asStateFlow()

    fun buscarMinhasPartidas() {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.getMyMatches(token ?: "")
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar partidas") }
        }
    }

    fun buscarPartidasPorUsername(username: String) {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.getMatchesByUsername(token ?: "", username)
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar partidas") }
        }
    }

    fun buscarHeadToHead(opponentUsername: String) {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.getHeadToHead(token ?: "", opponentUsername)
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar retrospecto") }
        }
    }

    fun criarPartida(opponentUsername: String, player1Score: Int, player2Score: Int) {
        viewModelScope.launch {
            _matchState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val request = MatchRequest(opponentUsername, player1Score, player2Score)
            val result = repository.createMatch(token ?: "", request)
            result.onSuccess { _matchState.value = Resource.Success(it) }
            result.onFailure { _matchState.value = Resource.Error(it.message ?: "Erro ao criar partida") }
        }
    }

    fun resetState() {
        _matchListState.value = null
        _matchState.value = null
    }
}
