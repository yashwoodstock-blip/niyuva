package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.NotificationConfig
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun saveConfig(config: NotificationConfig)
    fun getAllConfigs(): Flow<List<NotificationConfig>>
}
