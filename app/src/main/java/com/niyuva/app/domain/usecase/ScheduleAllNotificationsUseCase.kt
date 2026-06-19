package com.niyuva.app.domain.usecase

import com.niyuva.app.data.alarm.PeriodPrepAlarmManager
import com.niyuva.app.data.alarm.OvulationAlarmManager
import com.niyuva.app.data.work.NotificationScheduler
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.domain.repository.InsightRepository
import javax.inject.Inject

class ScheduleAllNotificationsUseCase @Inject constructor(
    private val notificationConfigDao: NotificationConfigDao,
    private val calculatePredictionsUseCase: CalculatePredictionsUseCase,
    private val periodPrepAlarmManager: PeriodPrepAlarmManager,
    private val ovulationAlarmManager: OvulationAlarmManager,
    private val notificationScheduler: NotificationScheduler,
    private val insightRepository: InsightRepository
) {
    suspend operator fun invoke() {
        val periodPrepConfig = notificationConfigDao.getByType("period_prep")
        val ovulationConfig = notificationConfigDao.getByType("ovulation")
        val dailyTipConfig = notificationConfigDao.getByType("daily_tip")
        val prediction = calculatePredictionsUseCase()

        // Always cancel existing prep notifications to avoid orphan alarms when states change
        periodPrepAlarmManager.cancel()

        if (periodPrepConfig?.enabled == true) {
            val hasIrregularityInsight = insightRepository.getLatestInsightByType("irregularity_flag") != null
            if (hasIrregularityInsight) {
                periodPrepAlarmManager.schedule(prediction.nextPeriodDate, 5, "irregular_early")
                periodPrepAlarmManager.schedule(prediction.nextPeriodDate, 1, "irregular_close")
            } else {
                periodPrepAlarmManager.schedule(prediction.nextPeriodDate, periodPrepConfig.daysBefore ?: 3, "standard")
            }
        }

        if (ovulationConfig?.enabled == true) {
            ovulationAlarmManager.schedule(prediction.ovulationDate)
        } else {
            ovulationAlarmManager.cancel()
        }

        if (dailyTipConfig?.enabled == true) {
            notificationScheduler.scheduleDailyTip(dailyTipConfig.timeOfDay ?: "08:00")
        } else {
            notificationScheduler.cancelDailyTip()
        }
    }
}
