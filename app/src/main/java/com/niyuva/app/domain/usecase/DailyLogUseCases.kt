package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

class SaveDailyLogUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(log: DailyLog, continueStreak: Boolean = false): StreakResult = repository.saveLog(log, continueStreak)
}

class GetLogForDateUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(date: LocalDate): DailyLog? = repository.getLogForDate(date)
}

class GetRecentLogsUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(count: Int): List<DailyLog> = repository.getRecentLogs(count)
}

class GetLogsInRangeUseCase @Inject constructor(
    private val repository: DailyLogRepository
) {
    suspend operator fun invoke(start: LocalDate, end: LocalDate): List<DailyLog> =
        repository.getLogsInRange(start, end)
}
