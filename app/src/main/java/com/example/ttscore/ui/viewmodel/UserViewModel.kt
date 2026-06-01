package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.remote.dto.UpdateProfileRequest
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
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
            if (token != null) {
                val result = repository.getMyProfile("Bearer $token")
                result.onSuccess { _myProfileState.value = Resource.Success(it) }
                result.onFailure { _myProfileState.value = Resource.Error(it.message ?: "Erro ao carregar perfil") }
            }
        }
    }

    fun atualizarAvatar(avatarUrl: String) {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val token = sessionManager.token.first()
            if (token != null) {
                val request = UpdateProfileRequest(null, avatarUrl)
                val result = repository.updateProfile("Bearer $token", request)
                result.onSuccess {
                    _userState.value = Resource.Success(it)
                    carregarMeuPerfil()
                }
                result.onFailure { _userState.value = Resource.Error(it.message ?: "Erro ao atualizar perfil") }
            }
        }
    }

    fun atualizarPerfil(username: String?, avatarUrl: String?) {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val token = sessionManager.token.first()
            if (token != null) {
                val request = UpdateProfileRequest(
                    username = username?.takeIf { it.isNotBlank() },
                    avatarUrl = avatarUrl?.takeIf { it.isNotBlank() }
                )
                val result = repository.updateProfile("Bearer $token", request)
                result.onSuccess {
                    _userState.value = Resource.Success(it)
                    carregarMeuPerfil()
                }
                result.onFailure { _userState.value = Resource.Error(it.message ?: "Erro ao atualizar perfil") }
            }
        }
    }

    fun buscarRanking() {
        viewModelScope.launch {
            _rankingState.value = Resource.Loading()
            val token = sessionManager.token.first()
            if (token != null) {
                val result = repository.getRanking("Bearer $token")
                result.onSuccess { _rankingState.value = Resource.Success(it) }
                result.onFailure { _rankingState.value = Resource.Error(it.message ?: "Erro ao buscar ranking") }
            }
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
            if (token != null) {
                val result = repository.searchUsers("Bearer $token", query)
                result.onSuccess { _searchState.value = Resource.Success(it) }
                result.onFailure { _searchState.value = Resource.Error(it.message ?: "Erro na busca") }
            }
        }
    }

    fun resetStates() {
        _userState.value = null
        _searchState.value = null
    }
}
