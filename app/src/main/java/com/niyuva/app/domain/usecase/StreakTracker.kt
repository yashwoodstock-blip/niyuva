package com.niyuva.app.domain.usecase

import com.niyuva.app.data.local.entity.StreakEntity
import com.niyuva.app.domain.repository.StreakRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreakTracker @Inject constructor(
    private val streakRepository: StreakRepository
) {
    suspend fun trackLog(today: LocalDate = LocalDate.now(), continueStreak: Boolean = false): StreakResult {
        val currentStreakEntity = streakRepository.getStreak()
        val todayStr = today.toString()
        
        if (currentStreakEntity == null) {
            // First time logging
            val newStreak = StreakEntity(
                id = 1,
                currentStreak = 1,
                longestStreak = 1,
                lastLogDate = todayStr,
                updatedAt = LocalDateTime.now().toString()
            )
            streakRepository.updateStreak(newStreak)
            return StreakResult.StreakIncremented(1)
        }

        val lastLogDateStr = currentStreakEntity.lastLogDate
        if (lastLogDateStr == null) {
            val newStreak = currentStreakEntity.copy(
                currentStreak = 1,
                longestStreak = maxOf(currentStreakEntity.longestStreak, 1),
                lastLogDate = todayStr,
                updatedAt = LocalDateTime.now().toString()
            )
            streakRepository.updateStreak(newStreak)
            return StreakResult.StreakIncremented(1)
        }

        val lastLogDate = LocalDate.parse(lastLogDateStr)
        if (lastLogDate == today) {
            // Logged today already, do nothing
            return StreakResult.NoChange
        }

        if (lastLogDate == today.minusDays(1) || continueStreak) {
            // Logged yesterday or forced continuation, increment streak
            val newCurrent = currentStreakEntity.currentStreak + 1
            val newLongest = maxOf(currentStreakEntity.longestStreak, newCurrent)
            val milestone7 = if (newCurrent >= 7 && currentStreakEntity.milestone7Shown == 0) 0 else currentStreakEntity.milestone7Shown
            val milestone30 = if (newCurrent >= 30 && currentStreakEntity.milestone30Shown == 0) 0 else currentStreakEntity.milestone30Shown
            val milestone100 = if (newCurrent >= 100 && currentStreakEntity.milestone100Shown == 0) 0 else currentStreakEntity.milestone100Shown

            val newStreak = currentStreakEntity.copy(
                currentStreak = newCurrent,
                longestStreak = newLongest,
                lastLogDate = todayStr,
                updatedAt = LocalDateTime.now().toString()
            )
            streakRepository.updateStreak(newStreak)

            // Check if a milestone was reached and not shown yet
            if (newCurrent == 7 && milestone7 == 0) {
                return StreakResult.MilestoneReached(7)
            }
            if (newCurrent == 30 && milestone30 == 0) {
                return StreakResult.MilestoneReached(30)
            }
            if (newCurrent == 100 && milestone100 == 0) {
                return StreakResult.MilestoneReached(100)
            }
            return StreakResult.StreakIncremented(newCurrent)
        }

        // Missed day (last log was before yesterday)
        val hadStreakBefore = currentStreakEntity.currentStreak > 1
        val newStreak = currentStreakEntity.copy(
            currentStreak = 1,
            lastLogDate = todayStr,
            updatedAt = LocalDateTime.now().toString()
        )
        streakRepository.updateStreak(newStreak)

        return if (hadStreakBefore) {
            StreakResult.StreakResetTo1
        } else {
            StreakResult.StreakIncremented(1)
        }
    }

    suspend fun resetStreakTo1() {
        val current = streakRepository.getStreak() ?: return
        val updated = current.copy(
            currentStreak = 1,
            updatedAt = java.time.LocalDateTime.now().toString()
        )
        streakRepository.updateStreak(updated)
    }

    suspend fun markMilestoneShown(milestone: Int) {
        val current = streakRepository.getStreak() ?: return
        val updated = when (milestone) {
            7 -> current.copy(milestone7Shown = 1)
            30 -> current.copy(milestone30Shown = 1)
            100 -> current.copy(milestone100Shown = 1)
            else -> current
        }
        streakRepository.updateStreak(updated)
    }
}

sealed class StreakResult {
    object NoChange : StreakResult()
    data class StreakIncremented(val currentStreak: Int) : StreakResult()
    object StreakResetTo1 : StreakResult()
    data class MilestoneReached(val milestone: Int) : StreakResult()
}
