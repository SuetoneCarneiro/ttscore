package com.example.ttscore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttscore.domain.model.Friendship
import com.example.ttscore.domain.repository.FriendshipRepository
import com.example.ttscore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendshipViewModel @Inject constructor(
    private val repository: FriendshipRepository
) : ViewModel() {

    private val _friendshipState = MutableStateFlow<Resource<Friendship>?>(null)
    val friendshipState = _friendshipState.asStateFlow()

    private val _actionState = MutableStateFlow<Resource<Unit>?>(null)
    val actionState = _actionState.asStateFlow()

    private val _pendingState = MutableStateFlow<Resource<List<Friendship>>?>(null)
    val pendingState = _pendingState.asStateFlow()

    fun enviarPedido(token: String, addresseeId: String, forceAccept: Boolean = false) {
        viewModelScope.launch {
            _friendshipState.value = Resource.Loading()
            val result = repository.sendFriendRequest(token, addresseeId, forceAccept)
            result.onSuccess { _friendshipState.value = Resource.Success(it) }
            result.onFailure { _friendshipState.value = Resource.Error(it.message ?: "Erro ao enviar pedido") }
        }
    }

    fun aceitarPedido(token: String, friendshipId: String) {
        viewModelScope.launch {
            _friendshipState.value = Resource.Loading()
            val result = repository.acceptFriendRequest(token, friendshipId)
            result.onSuccess { _friendshipState.value = Resource.Success(it) }
            result.onFailure { _friendshipState.value = Resource.Error(it.message ?: "Erro ao aceitar pedido") }
        }
    }

    fun removerAmizade(token: String, friendshipId: String) {
        viewModelScope.launch {
            _actionState.value = Resource.Loading()
            val result = repository.removeFriendship(token, friendshipId)
            result.onSuccess { _actionState.value = Resource.Success(Unit) }
            result.onFailure { _actionState.value = Resource.Error(it.message ?: "Erro ao remover amizade") }
        }
    }

    fun buscarPendentes(token: String) {
        viewModelScope.launch {
            _pendingState.value = Resource.Loading()
            val result = repository.getPendingFriendships(token)
            result.onSuccess { _pendingState.value = Resource.Success(it) }
            result.onFailure { _pendingState.value = Resource.Error(it.message ?: "Erro ao buscar pendentes") }
        }
    }

    fun resetStates() {
        _friendshipState.value = null
        _actionState.value = null
        _pendingState.value = null
    }
}
