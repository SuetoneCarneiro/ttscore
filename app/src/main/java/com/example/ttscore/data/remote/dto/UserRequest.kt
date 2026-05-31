package com.example.ttscore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String? = null,
    val avatarUrl: String? = null
)
