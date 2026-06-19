package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.CyclePrediction
import com.niyuva.app.domain.model.DischargeType
import com.niyuva.app.domain.model.PredictionConfidence
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.presentation.theme.CyclePhase
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculatePredictionsUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val userProfileRepository: UserProfileRepository,
    private val getLogsInRangeUseCase: GetLogsInRangeUseCase
) {
    suspend operator fun invoke(referenceDate: LocalDate = LocalDate.now()): CyclePrediction {
        return try {
            val cycles = cycleRepository.getRecentCycles(7)
            val profile = userProfileRepository.getProfile()

            when {
                cycles.isEmpty() -> fallbackPrediction(profile, referenceDate)
                cycles.size < 3  -> calculateEarly(
                    cycles.first().startDate,
                    profile?.averageCycleLength ?: 28,
                    profile?.averagePeriodLength ?: 5,
                    referenceDate
                )
                cycles.size < 7  -> calculateLearning(cycles, profile, referenceDate)
                else             -> calculateMature(cycles, profile, referenceDate)
            }
        } catch (e: Exception) {
            val profile = try { userProfileRepository.getProfile() } catch (ex: Exception) { null }
            fallbackPrediction(profile, referenceDate)
        }
    }

    private fun calculateEarly(
        lastPeriodStart: LocalDate,
        avgCycleLength: Int,
        avgPeriodLength: Int,
        referenceDate: LocalDate
    ): CyclePrediction {
        var nextPeriod = lastPeriodStart.plusDays(avgCycleLength.toLong())
        while (nextPeriod.isBefore(referenceDate)) {
            nextPeriod = nextPeriod.plusDays(avgCycleLength.toLong())
        }
        val currentCycleStartDate = nextPeriod.minusDays(avgCycleLength.toLong())
        val ovulation = nextPeriod.minusDays(14)
        val fertileStart = ovulation.minusDays(2)
        val fertileEnd = ovulation.plusDays(2)
        val dayInCycle = ChronoUnit.DAYS.between(currentCycleStartDate, referenceDate).toInt() + 1
        val phase = phaseFromDay(dayInCycle, avgPeriodLength, avgCycleLength)
        return CyclePrediction(
            nextPeriodDate = nextPeriod,
            ovulationDate = ovulation,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            currentPhase = phase,
            currentDayInCycle = dayInCycle.coerceAtLeast(1),
            daysUntilNextPeriod = ChronoUnit.DAYS.between(referenceDate, nextPeriod).toInt(),
            confidenceLevel = PredictionConfidence.LOW
        )
    }

    private fun calculateLearning(
        cycles: List<Cycle>,
        profile: UserProfile?,
        referenceDate: LocalDate
    ): CyclePrediction {
        val sortedCycles = cycles.sortedBy { it.startDate }
        val computedLengths = mutableListOf<Int>()
        for (i in 0 until sortedCycles.size - 1) {
            val len = ChronoUnit.DAYS.between(sortedCycles[i].startDate, sortedCycles[i+1].startDate).toInt()
            if (len > 0) {
                computedLengths.add(len)
            }
        }

        val lengths = computedLengths.reversed() // most recent first
        if (lengths.isEmpty()) {
            return calculateEarly(cycles.first().startDate, profile?.averageCycleLength ?: 28, profile?.averagePeriodLength ?: 5, referenceDate)
        }

        val mean = lengths.average()
        val filteredLengths = lengths.filter { kotlin.math.abs(it - mean) <= 10.0 }
        val finalLengths = if (filteredLengths.isNotEmpty()) filteredLengths else lengths

        val baseWeights = listOf(0.4, 0.3, 0.2, 0.1)
        val k = finalLengths.size
        val weights = List(k) { index ->
            if (index < baseWeights.size) baseWeights[index] else baseWeights.last()
        }
        val sumOfWeights = weights.sum()
        val normalizedWeights = weights.map { it / sumOfWeights }
        val weightedAverage = finalLengths.zip(normalizedWeights).sumOf { (length, weight) -> length * weight }

        val cycleLength = weightedAverage.roundToInt()

        val periodLength = cycles.mapNotNull { it.periodLength }.filter { it > 0 }.average().let {
            if (it.isNaN()) (profile?.averagePeriodLength ?: 5) else it.roundToInt()
        }

        val lastPeriodStart = cycles.first().startDate

        var nextPeriod = lastPeriodStart.plusDays(cycleLength.toLong())
        while (nextPeriod.isBefore(referenceDate)) {
            nextPeriod = nextPeriod.plusDays(cycleLength.toLong())
        }
        val currentCycleStartDate = nextPeriod.minusDays(cycleLength.toLong())
        val dayInCycle = ChronoUnit.DAYS.between(currentCycleStartDate, referenceDate).toInt() + 1

        val ovulation = nextPeriod.minusDays(14)
        val fertileStart = ovulation.minusDays(5)
        val fertileEnd = ovulation.plusDays(1)
        val phase = phaseFromDay(dayInCycle, periodLength, cycleLength)

        return CyclePrediction(
            nextPeriodDate = nextPeriod,
            ovulationDate = ovulation,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            currentPhase = phase,
            currentDayInCycle = dayInCycle.coerceAtLeast(1),
            daysUntilNextPeriod = ChronoUnit.DAYS.between(referenceDate, nextPeriod).toInt(),
            confidenceLevel = PredictionConfidence.MEDIUM
        )
    }

    private suspend fun calculateMature(
        cycles: List<Cycle>,
        profile: UserProfile?,
        referenceDate: LocalDate
    ): CyclePrediction {
        val sortedCycles = cycles.sortedBy { it.startDate }
        val computedLengths = mutableListOf<Int>()
        for (i in 0 until sortedCycles.size - 1) {
            val len = ChronoUnit.DAYS.between(sortedCycles[i].startDate, sortedCycles[i+1].startDate).toInt()
            if (len > 0) {
                computedLengths.add(len)
            }
        }

        val lengths = computedLengths.reversed() // most recent first
        if (lengths.isEmpty()) {
            return calculateEarly(cycles.first().startDate, profile?.averageCycleLength ?: 28, profile?.averagePeriodLength ?: 5, referenceDate)
        }

        val mean = lengths.average()
        val filteredLengths = lengths.filter { kotlin.math.abs(it - mean) <= 10.0 }
        val finalLengths = if (filteredLengths.isNotEmpty()) filteredLengths else lengths

        val baseWeights = listOf(0.25, 0.20, 0.15, 0.12, 0.10, 0.10, 0.08)
        val k = finalLengths.size
        val weights = List(k) { index ->
            if (index < baseWeights.size) baseWeights[index] else baseWeights.last()
        }
        val sumOfWeights = weights.sum()
        val normalizedWeights = weights.map { it / sumOfWeights }
        val weightedAverage = finalLengths.zip(normalizedWeights).sumOf { (length, weight) -> length * weight }

        val cycleLength = weightedAverage.roundToInt()

        val periodLength = cycles.mapNotNull { it.periodLength }.filter { it > 0 }.average().let {
            if (it.isNaN()) (profile?.averagePeriodLength ?: 5) else it.roundToInt()
        }

        val lastPeriodStart = cycles.first().startDate

        var nextPeriod = lastPeriodStart.plusDays(cycleLength.toLong())
        while (nextPeriod.isBefore(referenceDate)) {
            nextPeriod = nextPeriod.plusDays(cycleLength.toLong())
        }
        val currentCycleStartDate = nextPeriod.minusDays(cycleLength.toLong())
        val dayInCycle = ChronoUnit.DAYS.between(currentCycleStartDate, referenceDate).toInt() + 1

        val logs = try {
            getLogsInRangeUseCase(lastPeriodStart, referenceDate)
        } catch (e: Exception) {
            emptyList()
        }
        val stretchyLog = logs.filter { it.dischargeType == DischargeType.STRETCHY }
            .maxByOrNull { it.date }

        val ovulation = if (stretchyLog != null) {
            stretchyLog.date
        } else {
            nextPeriod.minusDays(14)
        }

        val fertileStart = ovulation.minusDays(5)
        val fertileEnd = ovulation.plusDays(1)
        val phase = phaseFromDay(dayInCycle, periodLength, cycleLength)

        return CyclePrediction(
            nextPeriodDate = nextPeriod,
            ovulationDate = ovulation,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            currentPhase = phase,
            currentDayInCycle = dayInCycle.coerceAtLeast(1),
            daysUntilNextPeriod = ChronoUnit.DAYS.between(referenceDate, nextPeriod).toInt(),
            confidenceLevel = PredictionConfidence.HIGH
        )
    }

    private fun fallbackPrediction(profile: UserProfile?, referenceDate: LocalDate): CyclePrediction {
        val avgCycleLength = profile?.averageCycleLength ?: 28
        val avgPeriodLength = profile?.averagePeriodLength ?: 5
        val lastPeriodStart = profile?.lastPeriodStartDate ?: referenceDate.minusDays(15)

        var nextPeriod = lastPeriodStart.plusDays(avgCycleLength.toLong())
        while (nextPeriod.isBefore(referenceDate)) {
            nextPeriod = nextPeriod.plusDays(avgCycleLength.toLong())
        }
        val currentCycleStartDate = nextPeriod.minusDays(avgCycleLength.toLong())
        val dayInCycle = ChronoUnit.DAYS.between(currentCycleStartDate, referenceDate).toInt() + 1
        val ovulation = nextPeriod.minusDays(14)
        val fertileStart = ovulation.minusDays(2)
        val fertileEnd = ovulation.plusDays(2)
        val phase = phaseFromDay(dayInCycle, avgPeriodLength, avgCycleLength)

        return CyclePrediction(
            nextPeriodDate = nextPeriod,
            ovulationDate = ovulation,
            fertileWindowStart = fertileStart,
            fertileWindowEnd = fertileEnd,
            currentPhase = phase,
            currentDayInCycle = dayInCycle.coerceAtLeast(1),
            daysUntilNextPeriod = ChronoUnit.DAYS.between(referenceDate, nextPeriod).toInt(),
            confidenceLevel = PredictionConfidence.LOW
        )
    }

    private fun phaseFromDay(dayInCycle: Int, periodLength: Int, cycleLength: Int): CyclePhase = when {
        dayInCycle <= periodLength -> CyclePhase.MENSTRUATION
        dayInCycle <= 13           -> CyclePhase.FOLLICULAR
        dayInCycle <= 16           -> CyclePhase.OVULATION
        else                       -> CyclePhase.LUTEAL
    }
}
