package com.example.ttscore.domain.model

data class Usuario(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: String,
    val wins: Int = 0,
    val losses: Int = 0
)
