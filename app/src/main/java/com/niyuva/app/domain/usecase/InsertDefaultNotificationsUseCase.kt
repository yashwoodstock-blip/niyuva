package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.NotificationConfig
import com.niyuva.app.domain.repository.NotificationRepository
import javax.inject.Inject

class InsertDefaultNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(
        periodReminderEnabled: Boolean,
        dailyTipEnabled: Boolean,
        ovulationReminderEnabled: Boolean
    ) {
        // period_prep: enabled per user choice, days_before = 3
        repository.saveConfig(
            NotificationConfig(
                type = "period_prep",
                enabled = periodReminderEnabled,
                daysBefore = 3
            )
        )
        // ovulation: enabled per user choice, daysBefore = 1
        repository.saveConfig(
            NotificationConfig(
                type = "ovulation",
                enabled = ovulationReminderEnabled,
                daysBefore = 1
            )
        )
        // daily_tip: enabled per user choice, time_of_day = "08:00"
        repository.saveConfig(
            NotificationConfig(
                type = "daily_tip",
                enabled = dailyTipEnabled,
                timeOfDay = "08:00"
            )
        )
        // irregular_cycle: enabled = true (always)
        repository.saveConfig(
            NotificationConfig(
                type = "irregular_cycle",
                enabled = true
            )
        )
        // custom: enabled = false
        repository.saveConfig(
            NotificationConfig(
                type = "custom",
                enabled = false
            )
        )
    }
}
