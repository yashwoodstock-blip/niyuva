package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.NotificationConfig
import com.niyuva.app.domain.repository.NotificationRepository
import javax.inject.Inject

class SaveNotificationConfigUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(config: NotificationConfig) {
        repository.saveConfig(config)
    }
}

class GetNotificationConfigsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() = repository.getAllConfigs()
}
