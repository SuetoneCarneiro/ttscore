package com.example.ttscore.data.repository

import com.example.ttscore.data.mapper.toDomain
import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.remote.dto.*
import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.domain.repository.UserRepository
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

class UserRepositoryImpl(
    private val api: TtscoreApi
) : UserRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            handleAuthResponse(response)
        } catch (e: IOException) {
            Result.failure(Exception("Falha na conexão. Verifique sua internet."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.register(request)
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): Result<AuthResponse> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Exception("Resposta vazia do servidor"))
        } else {
            val errorBody = response.errorBody()?.string()
            val message = try {
                val errorRes = json.decodeFromString<ErrorResponse>(errorBody ?: "")
                errorRes.message
            } catch (e: Exception) {
                "Erro ${response.code()}: ${response.message()}"
            }
            Result.failure(Exception(message))
        }
    }

    override suspend fun getMyProfile(token: String): Result<Usuario> {
        return try {
            val response = api.getMyProfile(formatToken(token))
            if (response.isSuccessful) {
                val userDto = response.body()
                if (userDto != null) {
                    Result.success(userDto.toDomain())
                } else {
                    Result.failure(Exception("Perfil não encontrado"))
                }
            } else {
                Result.failure(Exception("Erro ao buscar perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Usuario> {
        return try {
            val response = api.updateProfile(formatToken(token), request)
            if (response.isSuccessful) {
                val userDto = response.body()
                if (userDto != null) {
                    Result.success(userDto.toDomain())
                } else {
                    Result.failure(Exception("Erro ao atualizar perfil"))
                }
            } else {
                Result.failure(Exception("Erro na atualização: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(token: String, id: String): Result<Usuario> {
        return try {
            val response = api.getUserById(formatToken(token), id)
            if (response.isSuccessful) {
                val userDto = response.body()
                if (userDto != null) {
                    Result.success(userDto.toDomain())
                } else {
                    Result.failure(Exception("Usuário não encontrado"))
                }
            } else {
                Result.failure(Exception("Erro ao buscar usuário: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(token: String, query: String): Result<List<Usuario>> {
        return try {
            val response = api.searchUsers(formatToken(token), query)
            if (response.isSuccessful) {
                val dtos: List<UserResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { dto -> dto.toDomain() })
            } else {
                Result.failure(Exception("Erro na busca: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRanking(token: String): Result<List<Usuario>> {
        return try {
            val response = api.getRanking(formatToken(token))
            if (response.isSuccessful) {
                val dtos: List<UserResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { dto -> dto.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar ranking: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun formatToken(token: String) = if (token.startsWith("Bearer ")) token else "Bearer $token"
}
