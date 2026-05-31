package com.example.ttscore.data.mapper

import com.example.ttscore.data.remote.dto.MatchResponse
import com.example.ttscore.domain.model.Match

fun MatchResponse.toDomain(): Match {
    return Match(
        id = id,
        player1 = player1.toDomain(),
        player2 = player2.toDomain(),
        player1Score = player1Score,
        player2Score = player2Score,
        winnerId = winnerId,
        playedAt = playedAt
    )
}
