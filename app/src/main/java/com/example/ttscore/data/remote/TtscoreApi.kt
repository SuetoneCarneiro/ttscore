package com.example.ttscore.data.remote

import com.example.ttscore.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface TtscoreApi {

    // Auth
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Users
    @GET("api/users/me")
    suspend fun getMyProfile(@Header("Authorization") token: String): Response<UserResponse>

    @PUT("api/users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<UserResponse>

    @GET("api/users/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Response<List<UserResponse>>

    @GET("api/users/ranking")
    suspend fun getRanking(@Header("Authorization") token: String): Response<List<UserResponse>>

    // Matches
    @POST("api/matches")
    suspend fun createMatch(
        @Header("Authorization") token: String,
        @Body request: MatchRequest
    ): Response<MatchResponse>

    @GET("api/matches/me")
    suspend fun getMyMatches(@Header("Authorization") token: String): Response<List<MatchResponse>>

    @GET("api/matches/user/{username}")
    suspend fun getMatchesByUsername(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<List<MatchResponse>>

    @GET("api/matches/{id}")
    suspend fun getMatchById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<MatchResponse>

    @GET("api/matches/versus/{opponentUsername}")
    suspend fun getHeadToHead(
        @Header("Authorization") token: String,
        @Path("opponentUsername") opponentUsername: String
    ): Response<List<MatchResponse>>

    // Friendships
    @POST("api/friendships/request/{addresseeId}")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Path("addresseeId") addresseeId: String,
        @Query("forceAccept") forceAccept: Boolean = false
    ): Response<FriendshipResponse>

    @PATCH("api/friendships/{id}/accept")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<FriendshipResponse>

    @DELETE("api/friendships/{id}")
    suspend fun removeFriendship(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @GET("api/friendships")
    suspend fun getFriends(@Header("Authorization") token: String): Response<List<FriendshipResponse>>

    @GET("api/friendships/pending")
    suspend fun getPendingFriendships(@Header("Authorization") token: String): Response<List<FriendshipResponse>>
}
