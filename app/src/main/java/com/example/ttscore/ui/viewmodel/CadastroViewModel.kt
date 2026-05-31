package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.data.remote.dto.RegisterRequest
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _cadastroState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val cadastroState = _cadastroState.asStateFlow()

    fun cadastrar(username: String, email: String, pass: String) {
        viewModelScope.launch {
            _cadastroState.value = Resource.Loading()
            val request = RegisterRequest(username, email, pass)
            val result = repository.register(request)
            
            result.onSuccess { 
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
