package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.Insight
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.InsightRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class DetectIrregularCycleUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val insightRepository: InsightRepository
) {
    suspend operator fun invoke() {
        val cycles = cycleRepository.getRecentCycles(3)
        if (cycles.size < 3) return

        val lengths = calculateCycleLengths(cycles)
        if (lengths.size < 2) return // need at least 2 lengths to calculate variance/deviation

        val mean = lengths.average()
        val variance = lengths.maxOf { kotlin.math.abs(it - mean) }

        if (variance > 7) {
            val insight = Insight(
                date = LocalDate.now(),
                insightType = "irregularity_flag",
                content = """{"variance": $variance, "cycles": $lengths}""",
                source = "local"
            )
            insightRepository.saveInsight(insight)
        }
    }

    private fun calculateCycleLengths(cycles: List<Cycle>): List<Int> {
        val stored = cycles.mapNotNull { it.cycleLength }.filter { it > 0 }
        if (stored.size >= 2) return stored

        val lengths = mutableListOf<Int>()
        for (i in 0 until cycles.size - 1) {
            val days = ChronoUnit.DAYS.between(cycles[i+1].startDate, cycles[i].startDate).toInt()
            if (days > 0) {
                lengths.add(days)
            }
        }
        return lengths
    }
}
