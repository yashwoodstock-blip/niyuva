package com.niyuva.app.data.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.niyuva.app.R
import com.niyuva.app.data.NiyuvaNotificationChannels

class PeriodPrepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val notificationKey = intent.getStringExtra("notification_key")
        val daysBefore = intent.getIntExtra("days_before", 3)

        val titleText = when (notificationKey) {
            "irregular_early" -> context.getString(R.string.notification_early_title)
            "irregular_close" -> context.getString(R.string.notification_close_title)
            else -> context.getString(R.string.notification_standard_title)
        }

        val bodyText = when (notificationKey) {
            "irregular_early" -> context.getString(R.string.notification_early_body)
            "irregular_close" -> context.getString(R.string.notification_close_body)
            else -> context.getString(R.string.notification_standard_body, daysBefore)
        }

        val notification = NotificationCompat.Builder(context, NiyuvaNotificationChannels.CHANNEL_PERIOD_PREP)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyText))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_PERIOD_PREP, notification)
        } catch (e: SecurityException) {
            // Silently skip
        }
    }

    companion object {
        private const val NOTIFICATION_ID_PERIOD_PREP = 1002
    }
}
