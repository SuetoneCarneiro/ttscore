package com.example.ttscore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ttscore.domain.model.Usuario

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: String,
    val wins: Int,
    val losses: Int
) {
    fun toDomain(): Usuario = Usuario(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        wins = wins,
        losses = losses
    )

    companion object {
        fun fromDomain(user: Usuario): UserEntity = UserEntity(
            id = user.id,
            username = user.username,
            email = user.email,
            avatarUrl = user.avatarUrl,
            createdAt = user.createdAt,
            wins = user.wins,
            losses = user.losses
        )
    }
}
