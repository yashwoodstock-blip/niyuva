package com.niyuva.app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig()
)
data class GeminiContent(val role: String? = null, val parts: List<GeminiPart>)
data class GeminiPart(val text: String)
data class GeminiGenerationConfig(
    val maxOutputTokens: Int = 2000,
    val temperature: Float = 0.7f
)
data class GeminiResponse(val candidates: List<GeminiCandidate>)
data class GeminiCandidate(val content: GeminiContent)
