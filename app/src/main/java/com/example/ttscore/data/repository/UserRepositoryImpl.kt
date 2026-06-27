package com.example.ttscore.data.repository

import com.example.ttscore.data.local.dao.UserDao
import com.example.ttscore.data.local.entity.UserEntity
import com.example.ttscore.data.remote.dto.*
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val userEntity = userDao.getUserByUsername(request.username)
            if (userEntity != null) {
                Result.success(
                    AuthResponse(
                        token = "local-token",
                        user = userEntity.toUserResponse()
                    )
                )
            } else {
                Result.failure(Exception("Usuário não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val existing = userDao.getUserByUsername(request.username)
            if (existing != null) {
                return Result.failure(Exception("Usuário já existe"))
            }

            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                username = request.username,
                email = request.email,
                avatarUrl = null,
                createdAt = System.currentTimeMillis().toString(),
                wins = 0,
                losses = 0
            )
            userDao.insertUser(newUser)

            Result.success(
                AuthResponse(
                    token = "local-token",
                    user = newUser.toUserResponse()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyProfile(token: String): Result<Usuario> {
        return Result.failure(Exception("Perfil local não implementado (sem sessão)"))
    }

    override suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Usuario> {
        return Result.failure(Exception("Update profile não implementado"))
    }

    override suspend fun getUserById(token: String, id: String): Result<Usuario> {
        return try {
            val user = userDao.getUserById(id)
            if (user != null) Result.success(user.toDomain())
            else Result.failure(Exception("Usuário não encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(token: String, query: String): Result<List<Usuario>> {
        return try {
            val users = userDao.searchUsers(query)
            Result.success(users.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRanking(token: String): Result<List<Usuario>> {
        return try {
            val users = userDao.getRanking()
            Result.success(users.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
