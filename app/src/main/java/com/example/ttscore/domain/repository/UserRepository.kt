package com.example.ttscore.domain.repository

import com.example.ttscore.domain.model.Usuario
import com.example.ttscore.data.remote.dto.*

interface UserRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<AuthResponse>
    suspend fun getMyProfile(token: String): Result<Usuario>
    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<Usuario>
    suspend fun getUserById(token: String, id: String): Result<Usuario>
    suspend fun searchUsers(token: String, query: String): Result<List<Usuario>>
    suspend fun getRanking(token: String): Result<List<Usuario>>
}
