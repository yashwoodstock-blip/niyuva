package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import javax.inject.Inject

class BuildAiContextUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val userProfileRepository: UserProfileRepository,
    private val getCurrentPhaseUseCase: GetCurrentPhaseUseCase,
    private val calculatePredictionsUseCase: CalculatePredictionsUseCase
) {
    suspend operator fun invoke(): String {
        val phase = getCurrentPhaseUseCase()
        val prediction = calculatePredictionsUseCase()
        val recentLogs = dailyLogRepository.getRecentLogs(7)
        val cycles = cycleRepository.getRecentCycles(3)
        val profile = userProfileRepository.getProfile()

        // Build structured JSON context string
        return """
        {
          "current_cycle_day": ${(phase as? PhaseResult.Known)?.dayInCycle ?: "null"},
          "current_phase": "${(phase as? PhaseResult.Known)?.phase?.name ?: "unknown"}",
          "days_until_next_period": ${prediction.daysUntilNextPeriod},
          "recent_logs_last_7_days": ${buildLogsJson(recentLogs)},
          "cycle_summary": {
            "average_cycle_length": ${profile?.averageCycleLength ?: 28},
            "last_3_cycles": ${buildCyclesJson(cycles)},
            "irregularity_flag": false
          }
        }
        """.trimIndent()
    }

    private fun buildLogsJson(logs: List<DailyLog>): String {
        return logs.joinToString(prefix = "[", postfix = "]") { log ->
            """
            {
              "date": "${log.date}",
              "flow_level": ${log.flowLevel?.name?.let { "\"$it\"" } ?: "null"},
              "pain_level": ${log.painLevel?.name?.let { "\"$it\"" } ?: "null"},
              "discharge_type": ${log.dischargeType?.name?.let { "\"$it\"" } ?: "null"},
              "energy_level": ${log.energyLevel?.name?.let { "\"$it\"" } ?: "null"},
              "sleep_quality": ${log.sleepQuality?.name?.let { "\"$it\"" } ?: "null"},
              "moods": ${log.moods.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }}
            }
            """.trimIndent()
        }
    }

    private fun buildCyclesJson(cycles: List<Cycle>): String {
        return cycles.joinToString(prefix = "[", postfix = "]") { cycle ->
            """
            {
              "start_date": "${cycle.startDate}",
              "end_date": ${cycle.endDate?.let { "\"$it\"" } ?: "null"},
              "length": ${cycle.cycleLength ?: "null"}
            }
            """.trimIndent()
        }
    }
}
