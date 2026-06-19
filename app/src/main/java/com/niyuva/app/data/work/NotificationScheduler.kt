package com.niyuva.app.data.work

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.niyuva.app.data.local.dao.NotificationConfigDao
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val notificationConfigDao: NotificationConfigDao
) {
    fun scheduleDailyTip(timeOfDay: String = "08:00") {
        val parts = timeOfDay.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
        
        val now = LocalDateTime.now()
        var nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (nextRun.isBefore(now)) nextRun = nextRun.plusDays(1)
        val delay = ChronoUnit.MINUTES.between(now, nextRun)

        val request = PeriodicWorkRequestBuilder<DailyTipWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(false).build())
            .addTag("daily_tip")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_tip",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelDailyTip() {
        workManager.cancelAllWorkByTag("daily_tip")
    }
}
