package com.example.ttscore.data.mapper

import com.example.ttscore.data.remote.dto.UserResponse
import com.example.ttscore.domain.model.Usuario

fun UserResponse.toDomain(): Usuario {
    return Usuario(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        wins = wins,
        losses = losses
    )
}
