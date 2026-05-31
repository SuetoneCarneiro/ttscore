package com.example.ttscore.domain.repository

import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.model.Match

interface MatchRepository {
    suspend fun createMatch(token: String, request: MatchRequest): Result<Match>
    suspend fun getMyMatches(token: String): Result<List<Match>>
    suspend fun getMatchesByUsername(token: String, username: String): Result<List<Match>>
    suspend fun getMatchById(token: String, id: String): Result<Match>
    suspend fun getHeadToHead(token: String, opponentUsername: String): Result<List<Match>>
}
