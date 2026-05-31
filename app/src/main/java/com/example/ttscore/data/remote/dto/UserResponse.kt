package com.example.ttscore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val createdAt: String,
    val wins: Int = 0,
    val losses: Int = 0
)
