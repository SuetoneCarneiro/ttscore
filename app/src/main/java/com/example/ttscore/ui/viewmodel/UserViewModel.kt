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
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<Usuario>?>(null)
    val userState = _userState.asStateFlow()

    private val _rankingState = MutableStateFlow<Resource<List<Usuario>>?>(null)
    val rankingState = _rankingState.asStateFlow()

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
            val token = sessionManager.token.first()
            val result = repository.getRanking(token ?: "")
            result.onSuccess { _rankingState.value = Resource.Success(it) }
            result.onFailure { _rankingState.value = Resource.Error(it.message ?: "Erro ao carregar ranking") }
        }
    }

    fun resetStates() {
        _searchState.value = null
        _rankingState.value = null
        _userState.value = null
    }

    fun atualizarPerfil(username: String?, avatarUrl: String?) {
        // Local update logic can be added here if needed
    }
}
