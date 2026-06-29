package com.example.ttscore.data.repository

import com.example.ttscore.data.local.SessionManager
import com.example.ttscore.data.local.dao.MatchDao
import com.example.ttscore.data.local.dao.UserDao
import com.example.ttscore.data.local.entity.MatchEntity
import com.example.ttscore.data.remote.dto.MatchRequest
import com.example.ttscore.domain.model.Match
import com.example.ttscore.domain.repository.MatchRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class MatchRepositoryImpl(
    private val matchDao: MatchDao,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : MatchRepository {

    override suspend fun createMatch(token: String, request: MatchRequest): Result<Match> {
        return try {
            val p2 = userDao.getUserByUsername(request.opponentUsername) 
                ?: return Result.failure(Exception("Oponente não encontrado"))
            
            val currentUserId = sessionManager.userId.first()
                ?: return Result.failure(Exception("Sessão expirada. Faça login novamente."))

            val p1 = userDao.getUserById(currentUserId)
                ?: return Result.failure(Exception("Usuário logado não encontrado"))

            val matchEntity = MatchEntity(
                id = UUID.randomUUID().toString(),
                player1Id = p1.id,
                player2Id = p2.id,
                player1Score = request.player1Score,
                player2Score = request.player2Score,
                winnerId = if (request.player1Score > request.player2Score) p1.id else p2.id,
                playedAt = System.currentTimeMillis().toString()
            )
            
            matchDao.insertMatch(matchEntity)
            
            // Update wins/losses
            if (matchEntity.winnerId == p1.id) {
                userDao.insertUser(p1.copy(wins = p1.wins + 1))
                userDao.insertUser(p2.copy(losses = p2.losses + 1))
            } else {
                userDao.insertUser(p1.copy(losses = p1.losses + 1))
                userDao.insertUser(p2.copy(wins = p2.wins + 1))
            }

            Result.success(
                Match(
                    id = matchEntity.id,
                    player1 = p1.toDomain(),
                    player2 = p2.toDomain(),
                    player1Score = matchEntity.player1Score,
                    player2Score = matchEntity.player2Score,
                    winnerId = matchEntity.winnerId,
                    playedAt = matchEntity.playedAt
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyMatches(token: String): Result<List<Match>> {
        return try {
            val currentUserId = sessionManager.userId.first()
                ?: return Result.failure(Exception("Sessão expirada"))

            val allMatches = getAllMatchesInternal()
            val myMatches = allMatches.filter { 
                it.player1.id == currentUserId || it.player2.id == currentUserId 
            }
            Result.success(myMatches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchesByUsername(token: String, username: String): Result<List<Match>> {
        return try {
            val allMatches = getAllMatchesInternal()
            val userMatches = allMatches.filter { 
                it.player1.username == username || it.player2.username == username 
            }
            Result.success(userMatches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchById(token: String, id: String): Result<Match> {
        return try {
            val allMatches = getAllMatchesInternal()
            val match = allMatches.find { it.id == id }
            if (match != null) Result.success(match)
            else Result.failure(Exception("Partida não encontrada"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHeadToHead(token: String, opponentUsername: String): Result<List<Match>> {
        return try {
            val currentUserId = sessionManager.userId.first()
                ?: return Result.failure(Exception("Sessão expirada"))
            
            val allMatches = getAllMatchesInternal()
            val h2hMatches = allMatches.filter { match ->
                val involvesMe = match.player1.id == currentUserId || match.player2.id == currentUserId
                val involvesOpponent = match.player1.username == opponentUsername || match.player2.username == opponentUsername
                involvesMe && involvesOpponent
            }
            Result.success(h2hMatches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getAllMatchesInternal(): List<Match> {
        val matchEntities = matchDao.getAllMatches().first()
        return matchEntities.mapNotNull { entity ->
            val p1 = userDao.getUserById(entity.player1Id)
            val p2 = userDao.getUserById(entity.player2Id)
            if (p1 != null && p2 != null) {
                Match(
                    id = entity.id,
                    player1 = p1.toDomain(),
                    player2 = p2.toDomain(),
                    player1Score = entity.player1Score,
                    player2Score = entity.player2Score,
                    winnerId = entity.winnerId,
                    playedAt = entity.playedAt
                )
            } else null
        }
    }
}
