package com.niyuva.app.data.remote.api

data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    val max_tokens: Int = 4000,
    val temperature: Float = 0.7f
)

data class OpenAiMessage(val role: String, val content: String)

data class OpenAiChatResponse(val choices: List<OpenAiChoice>)

data class OpenAiChoice(val message: OpenAiMessage)
