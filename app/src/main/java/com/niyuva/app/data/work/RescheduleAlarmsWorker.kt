package com.niyuva.app.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.niyuva.app.domain.usecase.ScheduleAllNotificationsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RescheduleAlarmsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            scheduleAllNotificationsUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
