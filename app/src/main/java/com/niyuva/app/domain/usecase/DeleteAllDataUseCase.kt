package com.niyuva.app.domain.usecase

import com.niyuva.app.data.local.dao.ChatLogDao
import com.niyuva.app.data.local.dao.CycleDao
import com.niyuva.app.data.local.dao.DailyLogDao
import com.niyuva.app.data.local.dao.InsightDao
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.data.local.dao.UserProfileDao
import com.niyuva.app.data.local.entity.UserProfileEntity
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import java.time.LocalDateTime
import javax.inject.Inject

class DeleteAllDataUseCase @Inject constructor(
    private val cycleDao: CycleDao,
    private val dailyLogDao: DailyLogDao,
    private val chatLogDao: ChatLogDao,
    private val insightDao: InsightDao,
    private val notificationConfigDao: NotificationConfigDao,
    private val userProfileDao: UserProfileDao,
    private val preferences: NiyuvaPreferences
) {
    suspend operator fun invoke() {
        cycleDao.deleteAllCycles()
        dailyLogDao.deleteAllLogs()
        chatLogDao.deleteAllMessages()
        insightDao.deleteAllInsights()
        notificationConfigDao.deleteAll()
        
        // Reset profile to default state (keep onboardingComplete = false)
        userProfileDao.insertOrUpdateProfile(
            UserProfileEntity(
                id = 1,
                name = "",
                age = null,
                averageCycleLength = null,
                averagePeriodLength = null,
                lastPeriodStartDate = null,
                pinHash = null,
                securityQuestion = null,
                securityAnswerHash = null,
                aiEnabled = false,
                aiProvider = null,
                onboardingComplete = false,
                createdAt = LocalDateTime.now()
            )
        )
        preferences.clear()
    }
}
