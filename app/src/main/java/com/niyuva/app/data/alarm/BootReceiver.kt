package com.niyuva.app.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.niyuva.app.data.work.RescheduleAlarmsWorker

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val request = OneTimeWorkRequestBuilder<RescheduleAlarmsWorker>().build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
