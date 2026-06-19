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

class OvulationAlarmManager @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) {
    fun schedule(ovulationDate: LocalDate) {
        val reminderDate = ovulationDate.minusDays(1) // Ovulation kal ho sakta hai
        val reminderDateTime = LocalDateTime.of(reminderDate, LocalTime.of(9, 0))  // 9 AM
        val triggerAtMillis = reminderDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (triggerAtMillis <= System.currentTimeMillis()) return  // already past

        val intent = Intent(context, OvulationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REQUEST_CODE_OVULATION, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancel() {
        val intent = Intent(context, OvulationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REQUEST_CODE_OVULATION, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val REQUEST_CODE_OVULATION = 2002
    }
}
