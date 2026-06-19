package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllMessages(): Flow<List<ChatMessage>>
    suspend fun saveMessage(message: ChatMessage): Long
    suspend fun getRecentMessages(count: Int): List<ChatMessage>
    suspend fun clearChat()
}
