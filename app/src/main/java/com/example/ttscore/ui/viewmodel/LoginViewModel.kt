package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.data.remote.dto.AuthResponse
import com.example.ttscore.data.remote.dto.LoginRequest
import com.example.ttscore.domain.repository.UserRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val loginState = _loginState.asStateFlow()

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            val result = repository.login(LoginRequest(username, pass))
            
            result.onSuccess { 
                _loginState.value = Resource.Success(it) 
            }
            result.onFailure { 
                _loginState.value = Resource.Error(it.message ?: "Falha no login") 
            }
        }
    }

    fun resetState() {
        _loginState.value = null
    }
}
