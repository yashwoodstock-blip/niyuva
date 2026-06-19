package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatMessage>> {
        return repository.getAllMessages()
    }
}
