package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.presentation.screens.home.DayStripItem
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 * Builds a 7-item list representing the horizontal day strip on the Home screen.
 *
 * Layout: 3 days before today (indices 0-2), today (index 3), 3 days after (indices 4-6).
 *
 * Per item:
 *   - [DayStripItem.isLogged]    → true if a DailyLog entry exists for that date
 *   - [DayStripItem.isPeriodDay] → true if the date falls inside a recorded cycle
 *                                  (between startDate..endDate, or startDate..startDate+periodLength-1
 *                                   when endDate is null)
 *   - [DayStripItem.isFutureDay] → true if the date is after today
 */
class BuildDayStripUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository,
    private val cycleRepository: CycleRepository
) {
    suspend operator fun invoke(
        today: LocalDate = LocalDate.now(),
        selectedDate: LocalDate = LocalDate.now(),
        defaultPeriodLength: Int = 5
    ): List<DayStripItem> {
        val startWindow = today.minusDays(3)
        val endWindow   = today.plusDays(3)

        // Fetch recent cycles to determine period days (last 3 cycles is sufficient)
        val recentCycles = cycleRepository.getRecentCycles(3)

        // Fetch all logs from the start of the oldest cycle to endWindow
        val oldestCycleStart = recentCycles.lastOrNull()?.startDate ?: startWindow
        val logsInRange = dailyLogRepository.getLogsInRange(oldestCycleStart, endWindow)
        val loggedDates = logsInRange.map { it.date }.toSet()
        val logsMap = logsInRange.associateBy { it.date }

        return (0..6).map { offset ->
            val date = startWindow.plusDays(offset.toLong())

            // Find which cycle this date belongs to (the most recent cycle starting on or before date)
            val activeCycle = recentCycles.firstOrNull { !date.isBefore(it.startDate) }

            val isPeriodDay = if (activeCycle != null) {
                // Find all logs belonging to this cycle on or before the current date
                val cycleLogs = logsInRange.filter { it.date >= activeCycle.startDate && !it.date.isAfter(date) }

                val defaultPeriodEnd = activeCycle.startDate.plusDays((defaultPeriodLength - 1).toLong())
                val lastFlowLog = cycleLogs.lastOrNull { it.flowLevel != null }
                val basePeriodEnd = if (lastFlowLog != null && lastFlowLog.date.isAfter(defaultPeriodEnd)) {
                    lastFlowLog.date
                } else {
                    defaultPeriodEnd
                }

                val firstNoFlowLog = cycleLogs.firstOrNull { it.flowLevel == null }
                val periodEnd = if (firstNoFlowLog != null && !firstNoFlowLog.date.isAfter(basePeriodEnd)) {
                    firstNoFlowLog.date.minusDays(1)
                } else {
                    basePeriodEnd
                }

                val log = logsMap[date]
                if (log != null) {
                    log.flowLevel != null
                } else {
                    !date.isAfter(periodEnd)
                }
            } else {
                false
            }

            DayStripItem(
                date        = date,
                dayLetter   = date.toDayLetter(),
                dayNumber   = date.dayOfMonth,
                isToday     = date == today,
                isLogged    = date in loggedDates,
                isPeriodDay = isPeriodDay,
                isFutureDay = date.isAfter(today),
                isSelected  = date == selectedDate
            )
        }
    }

    // ─────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────

    private fun LocalDate.toDayLetter(): String = when (dayOfWeek) {
        DayOfWeek.MONDAY    -> "M"
        DayOfWeek.TUESDAY   -> "T"
        DayOfWeek.WEDNESDAY -> "W"
        DayOfWeek.THURSDAY  -> "T"
        DayOfWeek.FRIDAY    -> "F"
        DayOfWeek.SATURDAY  -> "S"
        DayOfWeek.SUNDAY    -> "S"
        else                -> "?"   // unreachable in Kotlin; guards against Java null interop
    }
}
