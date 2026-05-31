package com.example.ttscore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FriendshipResponse(
    val id: String,
    val requester: UserResponse,
    val addressee: UserResponse,
    val status: String,
    val createdAt: String
)
