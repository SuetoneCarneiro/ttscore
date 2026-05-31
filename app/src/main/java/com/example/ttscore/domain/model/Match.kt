package com.example.ttscore.domain.model

data class Match(
    val id: String,
    val player1: Usuario,
    val player2: Usuario,
    val player1Score: Int,
    val player2Score: Int,
    val winnerId: String,
    val playedAt: String
)
