package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.repository.MatchRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _matchState = MutableStateFlow<Resource<Match>?>(null)
    val matchState = _matchState.asStateFlow()

    private val _matchListState = MutableStateFlow<Resource<List<Match>>?>(null)
    val matchListState = _matchListState.asStateFlow()

    fun criarPartida(token: String, opponentUsername: String, p1Score: Int, p2Score: Int) {
        viewModelScope.launch {
            _matchState.value = Resource.Loading()
            val request = MatchRequest(opponentUsername, p1Score, p2Score)
            val result = repository.createMatch(token, request)
            result.onSuccess { _matchState.value = Resource.Success(it) }
            result.onFailure { _matchState.value = Resource.Error(it.message ?: "Erro ao criar partida") }
        }
    }

    fun buscarPartidasPorUsername(token: String, username: String) {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val result = repository.getMatchesByUsername(token, username)
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar partidas") }
        }
    }

    fun buscarHeadToHead(token: String, opponentUsername: String) {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val result = repository.getHeadToHead(token, opponentUsername)
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar histórico") }
        }
    }

    fun buscarMinhasPartidas(token: String) {
        viewModelScope.launch {
            _matchListState.value = Resource.Loading()
            val result = repository.getMyMatches(token)
            result.onSuccess { _matchListState.value = Resource.Success(it) }
            result.onFailure { _matchListState.value = Resource.Error(it.message ?: "Erro ao buscar partidas") }
        }
    }

    fun resetState() {
        _matchState.value = null
        _matchListState.value = null
    }
}
