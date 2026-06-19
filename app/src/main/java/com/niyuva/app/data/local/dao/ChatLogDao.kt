package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.niyuva.app.data.local.entity.ChatLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatLogDao {
    @Insert
    suspend fun insertMessage(message: ChatLogEntity): Long

    @Query("SELECT * FROM chat_logs ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatLogEntity>>

    @Query("SELECT * FROM chat_logs ORDER BY timestamp DESC LIMIT :count")
    suspend fun getRecentMessages(count: Int): List<ChatLogEntity>

    @Query("DELETE FROM chat_logs")
    suspend fun deleteAllMessages()

    @Query("SELECT COUNT(*) FROM chat_logs")
    suspend fun getMessageCount(): Int
}
