package com.example.ttscore.data.repository

import com.example.ttscore.data.local.dao.UserDao
import com.example.ttscore.data.local.entity.UserEntity
import com.example.ttscore.data.mapper.toDomain
import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.remote.dto.*
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val api: TtscoreApi
) : UserRepository {

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                userDao.insertUser(auth.user.toEntity())
                Result.success(auth)
            } else {
                Result.failure(Exception("Falha no login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                userDao.insertUser(auth.user.toEntity())
                Result.success(auth)
            } else {
                Result.failure(Exception("Falha no cadastro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyProfile(token: String): Result<Usuario> {
        return try {
            val response = api.getMyProfile("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Erro ao carregar perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Usuario> {
        return try {
            val response = api.updateProfile("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Erro ao atualizar perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(token: String, id: String): Result<Usuario> {
        return try {
            val response = api.getUserById("Bearer $token", id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Usuário não encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(token: String, query: String): Result<List<Usuario>> {
        return try {
            val response = api.searchUsers("Bearer $token", query)
            if (response.isSuccessful && response.body() != null) {
                val users = response.body()!!
                userDao.insertUsers(users.map { it.toEntity() })
                Result.success(users.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro na busca: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRanking(token: String): Result<List<Usuario>> {
        return try {
            val response = api.getRanking("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao carregar ranking: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun UserResponse.toEntity() = UserEntity(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        wins = wins,
        losses = losses
    )

    private fun UserEntity.toUserResponse() = UserResponse(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        wins = wins,
        losses = losses
    )
}
