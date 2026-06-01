package com.example.ttscore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: String? = null
)
