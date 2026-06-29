package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RankingUiState(
    val isLoading: Boolean = false,
    val ranking: List<Usuario> = emptyList(),
    val errorMessage: String? = null
)

class UserViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<Usuario>?>(null)
    val userState = _userState.asStateFlow()

    private val _rankingState = MutableStateFlow<Resource<List<Usuario>>?>(null)
    val rankingState = _rankingState.asStateFlow()

    private val _rankingUiState = MutableStateFlow(RankingUiState())
    val rankingUiState = _rankingUiState.asStateFlow()

    private val _searchState = MutableStateFlow<Resource<List<Usuario>>?>(null)
    val searchState = _searchState.asStateFlow()

    private val _myProfileState = MutableStateFlow<Resource<Usuario>?>(null)
    val myProfileState = _myProfileState.asStateFlow()

    fun carregarMeuPerfil() {
        viewModelScope.launch {
            _myProfileState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.getMyProfile(token ?: "")
            result.onSuccess { _myProfileState.value = Resource.Success(it) }
            result.onFailure { _myProfileState.value = Resource.Error(it.message ?: "Erro ao carregar perfil") }
        }
    }

    fun pesquisarUsuarios(query: String) {
        if (query.isBlank()) {
            _searchState.value = null
            return
        }
        viewModelScope.launch {
            _searchState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.searchUsers(token ?: "", query)
            result.onSuccess { _searchState.value = Resource.Success(it) }
            result.onFailure { _searchState.value = Resource.Error(it.message ?: "Erro ao buscar usuários") }
        }
    }

    fun carregarRanking() {
        viewModelScope.launch {
            _rankingState.value = Resource.Loading()
            _rankingUiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val token = sessionManager.token.first()
            val result = repository.getRanking(token ?: "")
            
            result.onSuccess { list -> 
                _rankingState.value = Resource.Success(list)
                _rankingUiState.update { it.copy(isLoading = false, ranking = list) }
            }
            result.onFailure { error -> 
                _rankingState.value = Resource.Error(error.message ?: "Erro ao carregar ranking")
                _rankingUiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun resetStates() {
        _searchState.value = null
        _rankingState.value = null
        _userState.value = null
    }

    fun atualizarPerfil(username: String?, avatarUrl: String?) {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val token = sessionManager.token.first()
            val result = repository.updateProfile(
                token ?: "",
                com.example.ttscore.data.remote.dto.UpdateProfileRequest(username, avatarUrl)
            )
            result.onSuccess { 
                _userState.value = Resource.Success(it)
                _myProfileState.value = Resource.Success(it) // Atualiza o perfil localmente
            }
            result.onFailure { 
                _userState.value = Resource.Error(it.message ?: "Erro ao atualizar perfil")
            }
        }
    }
}
