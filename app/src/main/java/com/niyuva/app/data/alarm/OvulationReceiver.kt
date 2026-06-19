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

class OvulationReceiver : BroadcastReceiver() {
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

        val bodyText = "Body thoda different feel kar sakti hai — normal hai 🌸"

        val notification = NotificationCompat.Builder(context, NiyuvaNotificationChannels.CHANNEL_OVULATION)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Ovulation kal ho sakta hai 🥚")
            .setContentText(bodyText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyText))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_OVULATION, notification)
        } catch (e: SecurityException) {
            // Silently skip
        }
    }

    companion object {
        private const val NOTIFICATION_ID_OVULATION = 1003
    }
}
