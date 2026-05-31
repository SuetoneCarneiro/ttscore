package com.example.ttscore.data.repository

import com.example.ttscore.data.mapper.toDomain
import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.remote.dto.FriendshipResponse
import com.example.ttscore.domain.model.Friendship
import com.example.ttscore.domain.repository.FriendshipRepository

class FriendshipRepositoryImpl(private val api: TtscoreApi) : FriendshipRepository {
    override suspend fun sendFriendRequest(token: String, addresseeId: String, forceAccept: Boolean): Result<Friendship> {
        return try {
            val response = api.sendFriendRequest(formatToken(token), addresseeId, forceAccept)
            if (response.isSuccessful) {
                val dto: FriendshipResponse? = response.body()
                if (dto != null) Result.success(dto.toDomain())
                else Result.failure(Exception("Resposta vazia"))
            } else {
                Result.failure(Exception("Erro ao enviar pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptFriendRequest(token: String, friendshipId: String): Result<Friendship> {
        return try {
            val response = api.acceptFriendRequest(formatToken(token), friendshipId)
            if (response.isSuccessful) {
                val dto: FriendshipResponse? = response.body()
                if (dto != null) Result.success(dto.toDomain())
                else Result.failure(Exception("Resposta vazia"))
            } else {
                Result.failure(Exception("Erro ao aceitar pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFriendship(token: String, friendshipId: String): Result<Unit> {
        return try {
            val response = api.removeFriendship(formatToken(token), friendshipId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao remover amizade: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFriends(token: String): Result<List<Friendship>> {
        return try {
            val response = api.getFriends(formatToken(token))
            if (response.isSuccessful) {
                val dtos: List<FriendshipResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { dto -> dto.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar amigos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPendingFriendships(token: String): Result<List<Friendship>> {
        return try {
            val response = api.getPendingFriendships(formatToken(token))
            if (response.isSuccessful) {
                val dtos: List<FriendshipResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { dto -> dto.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar pedidos pendentes: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun formatToken(token: String) = if (token.startsWith("Bearer ")) token else "Bearer $token"
}
