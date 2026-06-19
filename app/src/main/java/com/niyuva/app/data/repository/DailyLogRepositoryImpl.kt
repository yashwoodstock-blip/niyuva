package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.DailyLogDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.repository.DailyLogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

import com.niyuva.app.domain.usecase.StreakTracker
import com.niyuva.app.domain.usecase.StreakResult

@Singleton
class DailyLogRepositoryImpl @Inject constructor(
    private val dailyLogDao: DailyLogDao,
    private val streakTracker: dagger.Lazy<StreakTracker>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DailyLogRepository {

    override suspend fun saveLog(log: DailyLog, continueStreak: Boolean): StreakResult = withContext(ioDispatcher) {
        dailyLogDao.insertOrUpdateLog(log.toEntity())
        streakTracker.get().trackLog(log.date, continueStreak)
    }

    override suspend fun getLogForDate(date: LocalDate): DailyLog? = withContext(ioDispatcher) {
        dailyLogDao.getLogForDate(date.toString())?.toDomain()
    }

    override fun getAllLogs(): Flow<List<DailyLog>> {
        return dailyLogDao.getAllLogs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getRecentLogs(count: Int): List<DailyLog> = withContext(ioDispatcher) {
        dailyLogDao.getRecentLogs(count).map { it.toDomain() }
    }

    override suspend fun getLogsInRange(start: LocalDate, end: LocalDate): List<DailyLog> = withContext(ioDispatcher) {
        dailyLogDao.getLogsInRange(start.toString(), end.toString()).map { it.toDomain() }
    }
}
