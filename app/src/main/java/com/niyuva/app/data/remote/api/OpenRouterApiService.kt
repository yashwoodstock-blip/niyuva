package com.niyuva.app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenRouterApiService {
    @POST("api/v1/chat/completions")
    @Headers(
        "HTTP-Referer: https://niyuva.app",
        "X-Title: NIYUVA"
    )
    suspend fun chat(
        @Header("Authorization") authHeader: String,
        @Body request: OpenAiChatRequest
    ): Response<OpenAiChatResponse>
}
