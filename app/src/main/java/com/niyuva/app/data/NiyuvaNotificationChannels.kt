package com.niyuva.app.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NiyuvaNotificationChannels {
    const val CHANNEL_DAILY_TIP = "niyuva_daily_tip"
    const val CHANNEL_PERIOD_PREP = "niyuva_period_prep"
    const val CHANNEL_OVULATION = "niyuva_ovulation"
    const val CHANNEL_IRREGULARITY = "niyuva_irregularity"

    fun createAll(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java) ?: return
            // Daily Tip channel
            NotificationChannel(CHANNEL_DAILY_TIP, "Aaj ka tip", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Har subah ek phase-based tip"
                manager.createNotificationChannel(this)
            }
            // Period Prep channel
            NotificationChannel(CHANNEL_PERIOD_PREP, "Period reminder", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Period aane se pehle reminder"
                manager.createNotificationChannel(this)
            }
            // Ovulation channel
            NotificationChannel(CHANNEL_OVULATION, "Ovulation reminder", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Ovulation ke baare mein awareness"
                manager.createNotificationChannel(this)
            }
            // Irregularity channel
            NotificationChannel(CHANNEL_IRREGULARITY, "Cycle alert", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Irregular cycle ke baare mein"
                manager.createNotificationChannel(this)
            }
        }
    }
}
