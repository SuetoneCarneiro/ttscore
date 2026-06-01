package com.example.ttscore.data.remote

import com.example.ttscore.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.contains("/api/auth/")) {
            return chain.proceed(request)
        }

        val token = runBlocking { sessionManager.token.first() }
        val newRequest = request.newBuilder()
        
        token?.let {
            newRequest.addHeader("Authorization", "Bearer $it")
        }
        
        return chain.proceed(newRequest.build())
    }
}
