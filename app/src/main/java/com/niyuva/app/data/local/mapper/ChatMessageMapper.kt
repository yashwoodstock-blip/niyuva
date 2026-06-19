package com.niyuva.app.data.local.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.niyuva.app.data.local.entity.ChatLogEntity
import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.domain.model.ChatRole
import java.time.LocalDateTime

private val gson = Gson()

fun ChatLogEntity.toDomain(): ChatMessage {
    val mapType = object : TypeToken<Map<String, String>>() {}.type
    val extractedMap: Map<String, String>? = logsExtracted?.let {
        try {
            gson.fromJson<Map<String, String>>(it, mapType)
        } catch (e: Exception) {
            null
        }
    }
    return ChatMessage(
        id = id?.toLong() ?: 0L,
        timestamp = timestamp,
        role = ChatRole.fromString(role),
        message = message,
        logsExtracted = extractedMap
    )
}

fun ChatMessage.toEntity(createdAt: LocalDateTime = LocalDateTime.now()): ChatLogEntity {
    val extractedJson = logsExtracted?.let {
        gson.toJson(it)
    }
    return ChatLogEntity(
        id = if (id == 0L) null else id.toInt(),
        timestamp = timestamp,
        role = role.name.lowercase(),
        message = message,
        logsExtracted = extractedJson,
        createdAt = createdAt
    )
}
