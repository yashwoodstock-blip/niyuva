package com.niyuva.app.domain.model

data class NotificationConfig(
    val type: String,
    val enabled: Boolean,
    val daysBefore: Int? = null,
    val timeOfDay: String? = null,
    val customMessage: String? = null
)
