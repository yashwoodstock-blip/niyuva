package com.niyuva.app.domain.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: Long = 0,
    val timestamp: LocalDateTime,
    val role: ChatRole,
    val message: String,
    val logsExtracted: Map<String, String>? = null
)

enum class ChatRole {
    USER, DIDI;

    companion object {
        fun fromString(value: String?): ChatRole {
            if (value == null) return USER
            if (value.equals("SAARTHI", ignoreCase = true)) return DIDI
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: USER
        }
    }
}
