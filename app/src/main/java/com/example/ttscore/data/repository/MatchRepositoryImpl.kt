package com.example.ttscore.data.repository

import com.example.ttscore.data.mapper.toDomain
import com.example.ttscore.data.remote.TtscoreApi
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.data.remote.dto.MatchResponse
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.repository.MatchRepository

class MatchRepositoryImpl(private val api: TtscoreApi) : MatchRepository {
    override suspend fun createMatch(token: String, request: MatchRequest): Result<Match> {
        return try {
            val response = api.createMatch(formatToken(token), request)
            if (response.isSuccessful) {
                val dto: MatchResponse? = response.body()
                if (dto != null) Result.success(dto.toDomain())
                else Result.failure(Exception("Resposta vazia da API"))
            } else {
                Result.failure(Exception("Erro ao criar partida: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyMatches(token: String): Result<List<Match>> {
        return try {
            val response = api.getMyMatches(formatToken(token))
            if (response.isSuccessful) {
                val dtos: List<MatchResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar partidas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchesByUsername(token: String, username: String): Result<List<Match>> {
        return try {
            val response = api.getMatchesByUsername(formatToken(token), username)
            if (response.isSuccessful) {
                val dtos: List<MatchResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar partidas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchById(token: String, id: String): Result<Match> {
        return try {
            val response = api.getMatchById(formatToken(token), id)
            if (response.isSuccessful) {
                val dto = response.body()
                if (dto != null) {
                    Result.success(dto.toDomain())
                } else {
                    Result.failure(Exception("Partida não encontrada"))
                }
            } else {
                Result.failure(Exception("Erro ao buscar partida: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHeadToHead(token: String, opponentUsername: String): Result<List<Match>> {
        return try {
            val response = api.getHeadToHead(formatToken(token), opponentUsername)
            if (response.isSuccessful) {
                val dtos: List<MatchResponse> = response.body() ?: emptyList()
                Result.success(dtos.map { it.toDomain() })
            } else {
                Result.failure(Exception("Erro ao buscar histórico: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun formatToken(token: String) = if (token.startsWith("Bearer ")) token else "Bearer $token"
}
