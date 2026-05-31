package com.example.ttscore.data.mapper

import com.example.ttscore.data.remote.dto.FriendshipResponse
import com.example.ttscore.domain.model.Friendship

fun FriendshipResponse.toDomain(): Friendship {
    return Friendship(
        id = id,
        requester = requester.toDomain(),
        addressee = addressee.toDomain(),
        status = status,
        createdAt = createdAt
    )
}
