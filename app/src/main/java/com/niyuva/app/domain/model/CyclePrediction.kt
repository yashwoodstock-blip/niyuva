package com.niyuva.app.domain.model

import com.niyuva.app.presentation.theme.CyclePhase
import java.time.LocalDate

data class CyclePrediction(
    val nextPeriodDate: LocalDate,
    val ovulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val currentPhase: CyclePhase,
    val currentDayInCycle: Int,
    val daysUntilNextPeriod: Int,
    val confidenceLevel: PredictionConfidence
)

enum class PredictionConfidence {
    LOW, MEDIUM, HIGH;

    companion object {
        fun fromString(value: String?): PredictionConfidence {
            if (value == null) return MEDIUM
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

enum class ConfidenceLevel { ESTIMATED, LIKELY, CONFIDENT }

fun computeConfidenceLevel(
    loggedCycleCount: Int,
    isAiEnabled: Boolean,
    irregularityFlag: Boolean
): ConfidenceLevel {
    return when {
        loggedCycleCount <= 2 -> ConfidenceLevel.ESTIMATED
        loggedCycleCount in 3..5 && !isAiEnabled -> ConfidenceLevel.LIKELY
        loggedCycleCount >= 6 || isAiEnabled -> ConfidenceLevel.CONFIDENT
        else -> ConfidenceLevel.ESTIMATED
    }
}

