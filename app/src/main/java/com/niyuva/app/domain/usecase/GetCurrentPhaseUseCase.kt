package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.presentation.theme.CyclePhase
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

// ─────────────────────────────────────────────
// Phase resolution result
// ─────────────────────────────────────────────

sealed class PhaseResult {
    /** Returned when no profile or cycle data is present yet. */
    object Unknown : PhaseResult()

    /** Returned when enough data exists to calculate the phase. */
    data class Known(
        val phase: CyclePhase,
        val dayInCycle: Int,
        val totalCycleDays: Int
    ) : PhaseResult()
}

// ─────────────────────────────────────────────
// Use case
// ─────────────────────────────────────────────

/**
 * Resolves the current menstrual cycle phase from the latest cycle's start date and the
 * user profile's saved preferences.
 *
 * Returns [PhaseResult.Unknown] if no profile or cycle exists yet (e.g. fresh install).
 * Returns [PhaseResult.Known] otherwise.
 *
 * Phase boundaries (fixed offsets matching the IUI consensus):
 *   - Menstruation : days 1 .. periodLength
 *   - Follicular   : days periodLength+1 .. 13
 *   - Ovulation    : days 14 .. 16
 *   - Luteal       : days 17 .. end
 */
class GetCurrentPhaseUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(): PhaseResult {
        val profile = userProfileRepository.getProfile() ?: return PhaseResult.Unknown
        val latestCycle = cycleRepository.getLatestCycle() ?: return PhaseResult.Unknown

        val today = LocalDate.now()
        val startDate = latestCycle.startDate
        val cycleLength = profile.averageCycleLength ?: 28
        val periodLength = profile.averagePeriodLength ?: 5

        // Days since cycle start (1-indexed)
        val dayInCycle = ChronoUnit.DAYS.between(startDate, today).toInt() + 1

        // Guard: negative day means the cycle date is in the future — data inconsistency
        if (dayInCycle < 1) return PhaseResult.Unknown

        val phase = when {
            dayInCycle <= periodLength  -> CyclePhase.MENSTRUATION
            dayInCycle <= 13            -> CyclePhase.FOLLICULAR
            dayInCycle <= 16            -> CyclePhase.OVULATION
            else                        -> CyclePhase.LUTEAL
        }

        return PhaseResult.Known(
            phase = phase,
            dayInCycle = dayInCycle,
            totalCycleDays = cycleLength
        )
    }
}
