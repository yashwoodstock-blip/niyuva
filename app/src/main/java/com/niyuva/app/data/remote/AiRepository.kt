package com.niyuva.app.data.remote

import android.util.Log
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import com.niyuva.app.data.remote.api.GeminiApiService
import com.niyuva.app.data.remote.api.GeminiContent
import com.niyuva.app.data.remote.api.GeminiPart
import com.niyuva.app.data.remote.api.GeminiRequest
import com.niyuva.app.data.remote.api.GroqApiService
import com.niyuva.app.data.remote.api.OpenAiChatRequest
import com.niyuva.app.data.remote.api.OpenAiMessage
import com.niyuva.app.data.remote.api.OpenRouterApiService
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.usecase.BuildAiContextUseCase
import com.niyuva.app.domain.usecase.BuildDidiSystemPromptUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val geminiService: GeminiApiService,
    private val groqService: GroqApiService,
    private val openRouterService: OpenRouterApiService,
    private val preferences: NiyuvaPreferences,
    private val buildAiContextUseCase: BuildAiContextUseCase,
    private val buildSystemPromptUseCase: BuildDidiSystemPromptUseCase
) {
    suspend fun sendMessage(userMessage: String, provider: AiProvider): String = withContext(Dispatchers.IO) {
        @Suppress("SecretInLogs")
        val apiKey = preferences.getString(NiyuvaPreferences.Keys.KEY_AI_API_KEY)
            ?: throw IllegalStateException("No API key configured for AI features. Please add your key in Settings 💛")

        val contextJson = buildAiContextUseCase()
        val systemPrompt = buildSystemPromptUseCase(contextJson)

        when (provider) {
            AiProvider.GEMINI -> callGemini(apiKey, systemPrompt, userMessage)
            AiProvider.GROQ -> callGroq(apiKey, systemPrompt, userMessage)
            AiProvider.OPENROUTER -> callOpenRouter(apiKey, systemPrompt, userMessage)
        }
    }

    suspend fun sendAnalysisMessage(systemPrompt: String, userMessage: String, provider: AiProvider): String = withContext(Dispatchers.IO) {
        @Suppress("SecretInLogs")
        val apiKey = preferences.getString(NiyuvaPreferences.Keys.KEY_AI_API_KEY)
            ?: throw IllegalStateException("No API key configured for AI features. Please add your key in Settings 💛")

        when (provider) {
            AiProvider.GEMINI -> callGemini(apiKey, systemPrompt, userMessage)
            AiProvider.GROQ -> callGroq(apiKey, systemPrompt, userMessage)
            AiProvider.OPENROUTER -> callOpenRouter(apiKey, systemPrompt, userMessage)
        }
    }

    private val geminiModels = listOf(
        "gemini-2.0-flash",
        "gemini-1.5-flash",
        "gemini-1.5-flash-8b",
        "gemini-1.5-pro",
        "gemini-2.0-flash-exp",
        "gemini-3.5-flash",
        "gemini-3.1-flash",
        "gemini-3.1-flash-lite"
    )

    private val groqModels = listOf(
        "llama-3.3-70b-versatile",
        "llama-3.1-8b-instant",
        "deepseek-r1-distill-llama-70b",
        "mixtral-8x7b-32768",
        "gemma2-9b-it",
        "llama3-8b-8192"
    )

    private val openRouterModels = listOf(
        "google/gemma-2-9b-it:free",
        "mistralai/mistral-7b-instruct:free",
        "meta-llama/llama-3-8b-instruct:free",
        "meta-llama/llama-3.1-8b-instant:free",
        "meta-llama/llama-3.3-70b-instruct:free"
    )

    private suspend fun callGemini(apiKey: String, systemPrompt: String, userMessage: String): String {
        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = "$systemPrompt\n\nUser Question: $userMessage"))
                )
            )
        )
        val errors = mutableListOf<String>()
        for (model in geminiModels) {
            try {
                Log.d("AiRepository", "Trying Gemini model: $model")
                val response = geminiService.generateContent(model, apiKey, request)
                if (response.isSuccessful) {
                    val text = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!text.isNullOrBlank()) {
                        return text
                    }
                    errors.add("$model: returned empty response body")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    errors.add("$model: HTTP ${response.code()} - $errorMsg")
                }
            } catch (e: Exception) {
                errors.add("$model: Exception - ${e.localizedMessage ?: e.message}")
            }
        }
        throw Exception("All Gemini models failed:\n" + errors.joinToString("\n"))
    }

    private suspend fun callGroq(apiKey: String, systemPrompt: String, userMessage: String): String {
        val errors = mutableListOf<String>()
        for (model in groqModels) {
            try {
                Log.d("AiRepository", "Trying Groq model: $model")
                val request = OpenAiChatRequest(
                    model = model,
                    messages = listOf(
                        OpenAiMessage(role = "system", content = systemPrompt),
                        OpenAiMessage(role = "user", content = userMessage)
                    )
                )
                val response = groqService.chat("Bearer $apiKey", request)
                if (response.isSuccessful) {
                    val text = response.body()?.choices?.firstOrNull()?.message?.content
                    if (!text.isNullOrBlank()) {
                        return text
                    }
                    errors.add("$model: returned empty response body")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    errors.add("$model: HTTP ${response.code()} - $errorMsg")
                }
            } catch (e: Exception) {
                errors.add("$model: Exception - ${e.localizedMessage ?: e.message}")
            }
        }
        throw Exception("All Groq models failed:\n" + errors.joinToString("\n"))
    }

    private suspend fun callOpenRouter(apiKey: String, systemPrompt: String, userMessage: String): String {
        val errors = mutableListOf<String>()
        for (model in openRouterModels) {
            try {
                Log.d("AiRepository", "Trying OpenRouter model: $model")
                val request = OpenAiChatRequest(
                    model = model,
                    messages = listOf(
                        OpenAiMessage(role = "system", content = systemPrompt),
                        OpenAiMessage(role = "user", content = userMessage)
                    )
                )
                val response = openRouterService.chat("Bearer $apiKey", request = request)
                if (response.isSuccessful) {
                    val text = response.body()?.choices?.firstOrNull()?.message?.content
                    if (!text.isNullOrBlank()) {
                        return text
                    }
                    errors.add("$model: returned empty response body")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    errors.add("$model: HTTP ${response.code()} - $errorMsg")
                }
            } catch (e: Exception) {
                errors.add("$model: Exception - ${e.localizedMessage ?: e.message}")
            }
        }
        throw Exception("All OpenRouter models failed:\n" + errors.joinToString("\n"))
    }

    suspend fun testConnection(apiKey: String, provider: AiProvider): String? = withContext(Dispatchers.IO) {
        try {
            when (provider) {
                AiProvider.GEMINI -> {
                    val request = GeminiRequest(
                        contents = listOf(
                            GeminiContent(
                                parts = listOf(GeminiPart(text = "test connection"))
                            )
                        )
                    )
                    val errors = mutableListOf<String>()
                    for (model in geminiModels) {
                        try {
                            val response = geminiService.generateContent(model, apiKey, request)
                            if (response.isSuccessful) {
                                return@withContext null // Success
                            } else {
                                val errBody = response.errorBody()?.string() ?: "Unknown error"
                                errors.add("$model: HTTP ${response.code()} - $errBody")
                            }
                        } catch (e: Exception) {
                            errors.add("$model: ${e.localizedMessage ?: e.message}")
                        }
                    }
                    "Gemini connection failed:\n" + errors.joinToString("\n")
                }
                AiProvider.GROQ -> {
                    val errors = mutableListOf<String>()
                    for (model in groqModels) {
                        try {
                            val request = OpenAiChatRequest(
                                model = model,
                                messages = listOf(
                                    OpenAiMessage(role = "user", content = "test connection")
                                ),
                                max_tokens = 10
                            )
                            val response = groqService.chat("Bearer $apiKey", request)
                            if (response.isSuccessful) {
                                return@withContext null // Success
                            } else {
                                val errBody = response.errorBody()?.string() ?: "Unknown error"
                                errors.add("$model: HTTP ${response.code()} - $errBody")
                            }
                        } catch (e: Exception) {
                            errors.add("$model: ${e.localizedMessage ?: e.message}")
                        }
                    }
                    "Groq connection failed:\n" + errors.joinToString("\n")
                }
                AiProvider.OPENROUTER -> {
                    val errors = mutableListOf<String>()
                    for (model in openRouterModels) {
                        try {
                            val request = OpenAiChatRequest(
                                model = model,
                                messages = listOf(
                                    OpenAiMessage(role = "user", content = "test connection")
                                ),
                                max_tokens = 10
                            )
                            val response = openRouterService.chat("Bearer $apiKey", request = request)
                            if (response.isSuccessful) {
                                return@withContext null // Success
                            } else {
                                val errBody = response.errorBody()?.string() ?: "Unknown error"
                                errors.add("$model: HTTP ${response.code()} - $errBody")
                            }
                        } catch (e: Exception) {
                            errors.add("$model: ${e.localizedMessage ?: e.message}")
                        }
                    }
                    "OpenRouter connection failed:\n" + errors.joinToString("\n")
                }
            }
        } catch (e: Exception) {
            Log.e("AiRepository", "testConnection failed for provider $provider", e)
            "Connection failed: ${e.localizedMessage ?: e.message ?: "Unknown exception"}"
        }
    }
}
