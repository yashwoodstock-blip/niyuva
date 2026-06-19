package com.niyuva.app.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class PeriodPrepAlarmManager @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) {
    fun schedule(nextPeriodDate: LocalDate, daysBefore: Int, notificationKey: String) {
        val reminderDate = nextPeriodDate.minusDays(daysBefore.toLong())
        val reminderDateTime = LocalDateTime.of(reminderDate, LocalTime.of(9, 0))  // 9 AM
        val triggerAtMillis = reminderDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (triggerAtMillis <= System.currentTimeMillis()) return  // already past

        val intent = Intent(context, PeriodPrepReceiver::class.java).apply {
            putExtra("notification_key", notificationKey)
            putExtra("days_before", daysBefore)
        }

        val requestCode = when (notificationKey) {
            "irregular_early" -> REQUEST_CODE_IRREGULAR_EARLY
            "irregular_close" -> REQUEST_CODE_IRREGULAR_CLOSE
            else -> REQUEST_CODE_STANDARD
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancel() {
        val intent = Intent(context, PeriodPrepReceiver::class.java)
        listOf(REQUEST_CODE_STANDARD, REQUEST_CODE_IRREGULAR_EARLY, REQUEST_CODE_IRREGULAR_CLOSE).forEach { requestCode ->
            val pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        const val REQUEST_CODE_STANDARD = 2001
        const val REQUEST_CODE_IRREGULAR_EARLY = 2002
        const val REQUEST_CODE_IRREGULAR_CLOSE = 2003
    }
}
