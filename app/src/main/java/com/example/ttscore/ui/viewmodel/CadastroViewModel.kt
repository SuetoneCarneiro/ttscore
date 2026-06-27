package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.data.remote.dto.RegisterRequest
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CadastroViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _cadastroState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val cadastroState = _cadastroState.asStateFlow()

    fun cadastrar(username: String, email: String, pass: String) {
        viewModelScope.launch {
            _cadastroState.value = Resource.Loading()
            val request = RegisterRequest(username, email, pass)
            val result = repository.register(request)
            
            result.onSuccess { 
                viewModelScope.launch {
                    sessionManager.saveSession(it.token, it.user.id)
                }
                _cadastroState.value = Resource.Success(it)
            }
            result.onFailure { exception ->
                _cadastroState.value = Resource.Error(exception.message ?: "Erro ao cadastrar")
            }
        }
    }
    
    fun resetState() {
        _cadastroState.value = null
    }
}
