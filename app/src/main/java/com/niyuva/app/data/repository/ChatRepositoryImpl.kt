package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.ChatLogDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatLogDao: ChatLogDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {

    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return chatLogDao.getAllMessages().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveMessage(message: ChatMessage): Long = withContext(ioDispatcher) {
        chatLogDao.insertMessage(message.toEntity())
    }

    override suspend fun getRecentMessages(count: Int): List<ChatMessage> = withContext(ioDispatcher) {
        chatLogDao.getRecentMessages(count).map { it.toDomain() }
    }

    override suspend fun clearChat() = withContext(ioDispatcher) {
        chatLogDao.deleteAllMessages()
    }
}
