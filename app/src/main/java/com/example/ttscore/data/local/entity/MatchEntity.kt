package com.example.ttscore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.model.Usuario

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey
    val id: String,
    val player1Id: String,
    val player2Id: String,
    val player1Score: Int,
    val player2Score: Int,
    val winnerId: String,
    val playedAt: String
) {
    // Note: To reconstruct a domain Match, we'd need the Usuario objects.
    // This entity stores the IDs to simplify local storage.
    
    companion object {
        fun fromDomain(match: Match): MatchEntity = MatchEntity(
            id = match.id,
            player1Id = match.player1.id,
            player2Id = match.player2.id,
            player1Score = match.player1Score,
            player2Score = match.player2Score,
            winnerId = match.winnerId,
            playedAt = match.playedAt
        )
    }
}
