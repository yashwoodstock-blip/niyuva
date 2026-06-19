package com.niyuva.app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApiService {
    @POST("openai/v1/chat/completions")
    suspend fun chat(
        @Header("Authorization") authHeader: String,
        @Body request: OpenAiChatRequest
    ): Response<OpenAiChatResponse>
}
