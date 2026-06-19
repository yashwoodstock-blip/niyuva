package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.usecase.StreakResult
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface DailyLogRepository {
    suspend fun saveLog(log: DailyLog, continueStreak: Boolean = false): StreakResult
    suspend fun getLogForDate(date: LocalDate): DailyLog?
    fun getAllLogs(): Flow<List<DailyLog>>
    suspend fun getRecentLogs(count: Int): List<DailyLog>
    suspend fun getLogsInRange(start: LocalDate, end: LocalDate): List<DailyLog>
}
