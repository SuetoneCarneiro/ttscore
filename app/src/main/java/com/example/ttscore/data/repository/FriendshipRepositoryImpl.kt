package com.example.ttscore.data.repository

import com.example.ttscore.domain.model.Friendship
import com.example.ttscore.domain.repository.FriendshipRepository

class FriendshipRepositoryImpl : FriendshipRepository {
    override suspend fun sendFriendRequest(token: String, addresseeId: String, forceAccept: Boolean): Result<Friendship> {
        return Result.failure(Exception("Amizades locais não implementadas"))
    }

    override suspend fun acceptFriendRequest(token: String, friendshipId: String): Result<Friendship> {
        return Result.failure(Exception("Amizades locais não implementadas"))
    }

    override suspend fun removeFriendship(token: String, friendshipId: String): Result<Unit> {
        return Result.failure(Exception("Amizades locais não implementadas"))
    }

    override suspend fun getFriends(token: String): Result<List<Friendship>> {
        return Result.success(emptyList())
    }

    override suspend fun getPendingFriendships(token: String): Result<List<Friendship>> {
        return Result.success(emptyList())
    }
}
