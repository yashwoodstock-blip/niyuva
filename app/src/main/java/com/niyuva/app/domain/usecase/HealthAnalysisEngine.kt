package com.niyuva.app.domain.usecase

import com.niyuva.app.data.local.entity.AiAnalysisResultEntity
import com.niyuva.app.data.remote.AiRepository
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.repository.AiAnalysisRepository
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class HealthAnalysisEngine @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val aiRepository: AiRepository,
    private val aiAnalysisRepository: AiAnalysisRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend fun runAnalysis(
        onProgress: (Int, String) -> Unit
    ): Boolean {
        val profile = userProfileRepository.getProfile()
        if (profile == null || !profile.aiEnabled || profile.aiProvider == null) {
            onProgress(0, "Settings me key check karein! AI features active nahi hain 💛")
            return false
        }
        val provider = profile.aiProvider

        // Fetch Cycles
        val cycles = cycleRepository.getRecentCycles(6)
        val cycleSummary = if (cycles.isEmpty()) {
            "No cycle data logged yet."
        } else {
            cycles.mapIndexed { index, cycle ->
                "Cycle ${index + 1}: length = ${cycle.cycleLength ?: "unknown"} days, period duration = ${cycle.periodLength} days"
            }.joinToString("\n")
        }

        // Fetch Logs
        val logs = dailyLogRepository.getLogsInRange(LocalDate.now().minusMonths(6), LocalDate.now())
        val logSummary = if (logs.isEmpty()) {
            "No daily symptoms logged."
        } else {
            logs.take(30).map { log ->
                val cycleStart = cycles.find { !log.date.isBefore(it.startDate) && (it.endDate == null || log.date.isBefore(it.endDate)) }?.startDate
                val relativeDayStr = if (cycleStart != null) {
                    "Day ${ChronoUnit.DAYS.between(cycleStart, log.date) + 1} of cycle"
                } else {
                    "Relative offset: ${ChronoUnit.DAYS.between(log.date, LocalDate.now())} days ago"
                }
                
                val symptoms = mutableListOf<String>()
                log.flowLevel?.let { symptoms.add("Flow: ${it.name.lowercase()}") }
                log.painLevel?.let { symptoms.add("Pain: ${it.name.lowercase()}") }
                log.energyLevel?.let { symptoms.add("Energy: ${it.name.lowercase()}") }
                if (log.moods.isNotEmpty()) {
                    symptoms.add("Moods: ${log.moods.joinToString(", ")}")
                }
                "- $relativeDayStr -> ${symptoms.joinToString("; ")}"
            }.joinToString("\n")
        }

        try {
            onProgress(1, "Analyzing cycle patterns...")
            val response1 = aiRepository.sendAnalysisMessage(
                systemPrompt = "You are NIYUVA's AI Health Analysis Engine. Analyze the user's cycle tracking history. Detect patterns like cycle regularity, phase averages, and flag any potential variances. Provide your analysis in warm, clear Hinglish. Do NOT mention the user's name or any calendar dates. You MUST format your response as a JSON object containing three fields: 'regularity_status' (either 'Regular', 'Irregular', or 'Not enough data'), 'data_confidence' (either 'low', 'moderate', or 'high'), and 'analysis_text' (your detailed Hinglish analysis). Output ONLY the JSON object, do not wrap in markdown blocks or any other formatting.",
                userMessage = "Analyze my cycle history:\n$cycleSummary",
                provider = provider
            )
            val entity1 = AiAnalysisResultEntity(
                analysisDate = LocalDate.now().toString(),
                analysisType = "cycle_patterns",
                chunkSequence = 1,
                rawResponse = response1,
                parsedSummary = response1,
                status = "success",
                createdAt = LocalDateTime.now().toString()
            )
            aiAnalysisRepository.insertOrUpdateResult(entity1)

            delay(2000)

            onProgress(2, "Analyzing symptom correlations...")
            val response2 = aiRepository.sendAnalysisMessage(
                systemPrompt = "You are NIYUVA's AI Health Analysis Engine. Analyze the user's logged symptoms (flow, pain, energy, mood) over their cycles to find correlations. Provide your analysis in warm, clear Hinglish. Do NOT mention the user's name or any calendar dates.",
                userMessage = "Analyze my symptoms correlations:\n$logSummary",
                provider = provider
            )
            val entity2 = AiAnalysisResultEntity(
                analysisDate = LocalDate.now().toString(),
                analysisType = "symptoms",
                chunkSequence = 2,
                rawResponse = response2,
                parsedSummary = response2,
                status = "success",
                createdAt = LocalDateTime.now().toString()
            )
            aiAnalysisRepository.insertOrUpdateResult(entity2)

            delay(2000)

            onProgress(3, "Synthesizing overall health patterns...")
            val response3 = aiRepository.sendAnalysisMessage(
                systemPrompt = "You are NIYUVA's AI Health Analysis Engine. Based on the cycle history and symptom trends, synthesize a comprehensive Hinglish summary with 3-4 actionable self-care recommendations. Provide your analysis in warm, clear Hinglish. Do NOT mention the user's name or any calendar dates.",
                userMessage = "Synthesize analysis:\nCycle summary:\n$cycleSummary\n\nSymptom summary:\n$logSummary",
                provider = provider
            )
            val entity3 = AiAnalysisResultEntity(
                analysisDate = LocalDate.now().toString(),
                analysisType = "synthesis",
                chunkSequence = 3,
                rawResponse = response3,
                parsedSummary = response3,
                status = "success",
                createdAt = LocalDateTime.now().toString()
            )
            aiAnalysisRepository.insertOrUpdateResult(entity3)

            return true
        } catch (e: Exception) {
            onProgress(0, "Error: ${e.localizedMessage ?: "Unknown error occurred"}")
            return false
        }
    }
}
