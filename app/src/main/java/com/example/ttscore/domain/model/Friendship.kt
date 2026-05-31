package com.example.ttscore.domain.model

data class Friendship(
    val id: String,
    val requester: Usuario,
    val addressee: Usuario,
    val status: String,
    val createdAt: String
)
