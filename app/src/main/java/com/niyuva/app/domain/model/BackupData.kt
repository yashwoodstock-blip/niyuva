package com.niyuva.app.domain.model

data class BackupData(
    val version: Int,
    val exportedAt: String,
    val profile: UserProfile?,
    val cycles: List<Cycle>,
    val recentLogs: List<DailyLog>,
    val messages: List<ChatMessage>
)
