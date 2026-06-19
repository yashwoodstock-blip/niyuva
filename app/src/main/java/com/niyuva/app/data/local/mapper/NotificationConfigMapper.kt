package com.niyuva.app.data.local.mapper

import com.niyuva.app.data.local.entity.NotificationConfigEntity
import com.niyuva.app.domain.model.NotificationConfig

fun NotificationConfigEntity.toDomain(): NotificationConfig {
    return NotificationConfig(
        type = type,
        enabled = enabled,
        daysBefore = daysBefore,
        timeOfDay = timeOfDay,
        customMessage = customMessage
    )
}

fun NotificationConfig.toEntity(): NotificationConfigEntity {
    return NotificationConfigEntity(
        type = type,
        enabled = enabled,
        daysBefore = daysBefore,
        timeOfDay = timeOfDay,
        customMessage = customMessage
    )
}
