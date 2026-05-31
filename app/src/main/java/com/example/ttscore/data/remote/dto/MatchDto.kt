package com.example.ttscore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MatchRequest(
    val opponentUsername: String,
    val player1Score: Int,
    val player2Score: Int
)

@Serializable
data class MatchResponse(
    val id: String,
    val player1: UserResponse,
    val player2: UserResponse,
    val player1Score: Int,
    val player2Score: Int,
    val winnerId: String,
    val playedAt: String
)
