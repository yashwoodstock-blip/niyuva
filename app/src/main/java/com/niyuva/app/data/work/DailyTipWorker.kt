package com.niyuva.app.data.work

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.niyuva.app.R
import com.niyuva.app.data.NiyuvaNotificationChannels
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.domain.usecase.GetCurrentPhaseUseCase
import com.niyuva.app.domain.usecase.GetDailyTipUseCase
import com.niyuva.app.domain.usecase.PhaseResult
import com.niyuva.app.presentation.theme.CyclePhase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyTipWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getCurrentPhaseUseCase: GetCurrentPhaseUseCase,
    private val getDailyTipUseCase: GetDailyTipUseCase,
    private val notificationConfigDao: NotificationConfigDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val config = notificationConfigDao.getByType("daily_tip") ?: return Result.success()
        if (!config.enabled) return Result.success()

        // Check permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.success()
            }
        }

        val phaseResult = getCurrentPhaseUseCase()
        val (phase, dayInCycle) = when (phaseResult) {
            is PhaseResult.Known -> Pair(phaseResult.phase, phaseResult.dayInCycle)
            else -> Pair(CyclePhase.FOLLICULAR, 1)
        }
        val tip = getDailyTipUseCase(phase, dayInCycle)

        // Build and show notification
        val notification = NotificationCompat.Builder(applicationContext, NiyuvaNotificationChannels.CHANNEL_DAILY_TIP)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Aaj ka tip 🌸")
            .setContentText(tip.tipText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(tip.tipText))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(applicationContext)
                .notify(NOTIFICATION_ID_DAILY_TIP, notification)
        } catch (e: SecurityException) {
            // Silently skip if permission not granted
        }

        return Result.success()
    }

    companion object {
        private const val NOTIFICATION_ID_DAILY_TIP = 1001
    }
}
