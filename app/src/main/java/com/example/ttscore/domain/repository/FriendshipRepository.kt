package com.example.ttscore.domain.repository

import com.example.ttscore.domain.model.Friendship

interface FriendshipRepository {
    suspend fun sendFriendRequest(token: String, addresseeId: String, forceAccept: Boolean = false): Result<Friendship>
    suspend fun acceptFriendRequest(token: String, friendshipId: String): Result<Friendship>
    suspend fun removeFriendship(token: String, friendshipId: String): Result<Unit>
    suspend fun getFriends(token: String): Result<List<Friendship>>
    suspend fun getPendingFriendships(token: String): Result<List<Friendship>>
}
