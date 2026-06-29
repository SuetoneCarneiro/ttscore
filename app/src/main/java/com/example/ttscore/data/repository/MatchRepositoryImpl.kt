package com.example.ttscore.data.repository

import com.example.ttscore.data.mapper.toDomain
import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.repository.MatchRepository

class MatchRepositoryImpl(
    private val api: TtscoreApi
) : MatchRepository {

    override suspend fun createMatch(token: String, request: MatchRequest): Result<Match> {
        return try {
            val response = api.createMatch("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Erro ao salvar partida: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyMatches(token: String): Result<List<Match>> {
        return try {
            val response = api.getMyMatches("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar partidas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchesByUsername(token: String, username: String): Result<List<Match>> {
        return try {
            val response = api.getMatchesByUsername("Bearer $token", username)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar partidas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchById(token: String, id: String): Result<Match> {
        return try {
            val response = api.getMatchById("Bearer $token", id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Partida não encontrada: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHeadToHead(token: String, opponentUsername: String): Result<List<Match>> {
        return try {
            val response = api.getHeadToHead("Bearer $token", opponentUsername)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar histórico: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
