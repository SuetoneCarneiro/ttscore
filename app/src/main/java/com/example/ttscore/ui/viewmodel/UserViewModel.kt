package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.remote.dto.UpdateProfileRequest
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<Usuario>?>(null)
    val userState = _userState.asStateFlow()

    private val _rankingState = MutableStateFlow<Resource<List<Usuario>>?>(null)
    val rankingState = _rankingState.asStateFlow()

    fun atualizarPerfil(token: String, username: String?, avatarUrl: String?) {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val request = UpdateProfileRequest(username, avatarUrl)
            val result = repository.updateProfile(token, request)
            result.onSuccess { _userState.value = Resource.Success(it) }
            result.onFailure { _userState.value = Resource.Error(it.message ?: "Erro ao atualizar perfil") }
        }
    }

    fun buscarRanking(token: String) {
        viewModelScope.launch {
            _rankingState.value = Resource.Loading()
            val result = repository.getRanking(token)
            result.onSuccess { _rankingState.value = Resource.Success(it) }
            result.onFailure { _rankingState.value = Resource.Error(it.message ?: "Erro ao buscar ranking") }
        }
    }

    fun resetStates() {
        _userState.value = null
        _rankingState.value = null
    }
}
