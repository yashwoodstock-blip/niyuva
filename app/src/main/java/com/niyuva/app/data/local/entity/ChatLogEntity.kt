package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "chat_logs")
data class ChatLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "timestamp")
    val timestamp: LocalDateTime,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "logs_extracted")
    val logsExtracted: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)
