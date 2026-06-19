package com.niyuva.app.presentation.screens.home

import android.graphics.PointF
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.presentation.components.ChartLine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

enum class AnalyticsChartType { ENERGY, MOOD, PAIN, FLOW }
enum class AnalyticsTimePeriod(val months: Int) { THREE(3), SIX(6), TWELVE(12) }

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val selectedChartType: AnalyticsChartType = AnalyticsChartType.ENERGY,
    val selectedTimePeriod: AnalyticsTimePeriod = AnalyticsTimePeriod.THREE,
    val chartLines: List<ChartLine> = emptyList(),
    val yAxisLabels: List<String> = emptyList(),
    val xAxisLabels: List<String> = listOf("Menstruation", "Follicular", "Ovulation", "Luteal"),
    val isEmpty: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val dailyLogRepository: DailyLogRepository,
    private val cycleRepository: CycleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    fun selectChartType(type: AnalyticsChartType) {
        _uiState.value = _uiState.value.copy(selectedChartType = type)
        loadAnalyticsData()
    }

    fun selectTimePeriod(period: AnalyticsTimePeriod) {
        _uiState.value = _uiState.value.copy(selectedTimePeriod = period)
        loadAnalyticsData()
    }

    fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val chartType = _uiState.value.selectedChartType
            val period = _uiState.value.selectedTimePeriod
            
            withContext(Dispatchers.IO) {
                runCatching {
                    val endDate = LocalDate.now()
                    val startDate = endDate.minusMonths(period.months.toLong())
                    
                    val logs = dailyLogRepository.getLogsInRange(startDate, endDate)
                    val cycles = cycleRepository.getRecentCycles(12) // fetch enough cycles to cover the period
                    
                    if (logs.isEmpty() || cycles.isEmpty()) {
                        return@runCatching AnalyticsUiState(
                            isLoading = false,
                            selectedChartType = chartType,
                            selectedTimePeriod = period,
                            chartLines = emptyList(),
                            isEmpty = true
                        )
                    }

                    val lines = when (chartType) {
                        AnalyticsChartType.ENERGY -> {
                            val points = mutableListOf<PointF>()
                            // Group logs by cycle phase day to compute average
                            val dayValues = mutableMapOf<Int, MutableList<Float>>()
                            logs.forEach { log ->
                                val cycle = cycles.find { c ->
                                    val start = c.startDate
                                    val end = c.endDate ?: start.plusDays((c.cycleLength ?: 28).toLong())
                                    !log.date.isBefore(start) && !log.date.isAfter(end)
                                }
                                if (cycle != null) {
                                    val dayInCycle = ChronoUnit.DAYS.between(cycle.startDate, log.date).toInt() + 1
                                    val value = when (log.energyLevel) {
                                        EnergyLevel.LOW -> 0.0f
                                        EnergyLevel.NORMAL -> 0.5f
                                        EnergyLevel.HIGH -> 1.0f
                                        else -> null
                                    }
                                    if (value != null && dayInCycle in 1..(cycle.cycleLength ?: 28)) {
                                        dayValues.getOrPut(dayInCycle) { mutableListOf() }.add(value)
                                    }
                                }
                            }
                            
                            val cycleLength = 28
                            val periodLength = 5
                            dayValues.forEach { (day, list) ->
                                val avg = list.average().toFloat()
                                val x = getNormalizedCycleX(day, periodLength, cycleLength)
                                points.add(PointF(x, avg))
                            }

                            if (points.isEmpty()) emptyList()
                            else listOf(ChartLine(label = "", points = points.sortedBy { it.x }, color = Color(0xFF8B5E6D))) // Plum
                        }
                        AnalyticsChartType.MOOD -> {
                            val points = mutableListOf<PointF>()
                            val dayValues = mutableMapOf<Int, MutableList<Float>>()
                            logs.forEach { log ->
                                val cycle = cycles.find { c ->
                                    val start = c.startDate
                                    val end = c.endDate ?: start.plusDays((c.cycleLength ?: 28).toLong())
                                    !log.date.isBefore(start) && !log.date.isAfter(end)
                                }
                                if (cycle != null) {
                                    val dayInCycle = ChronoUnit.DAYS.between(cycle.startDate, log.date).toInt() + 1
                                    val moodVal = mapMoodsToScore(log.moods)
                                    if (moodVal != null && dayInCycle in 1..(cycle.cycleLength ?: 28)) {
                                        // map 1..5 scale to 0..1 scale
                                        val normalized = (moodVal - 1f) / 4f
                                        dayValues.getOrPut(dayInCycle) { mutableListOf() }.add(normalized)
                                    }
                                }
                            }
                            val cycleLength = 28
                            val periodLength = 5
                            dayValues.forEach { (day, list) ->
                                val avg = list.average().toFloat()
                                val x = getNormalizedCycleX(day, periodLength, cycleLength)
                                points.add(PointF(x, avg))
                            }
                            if (points.isEmpty()) emptyList()
                            else listOf(ChartLine(label = "", points = points.sortedBy { it.x }, color = Color(0xFFE8965A))) // Warm Orange
                        }
                        AnalyticsChartType.PAIN -> {
                            val points = mutableListOf<PointF>()
                            val dayValues = mutableMapOf<Int, MutableList<Float>>()
                            logs.forEach { log ->
                                val cycle = cycles.find { c ->
                                    val start = c.startDate
                                    val end = c.endDate ?: start.plusDays((c.cycleLength ?: 28).toLong())
                                    !log.date.isBefore(start) && !log.date.isAfter(end)
                                }
                                if (cycle != null) {
                                    val dayInCycle = ChronoUnit.DAYS.between(cycle.startDate, log.date).toInt() + 1
                                    val painVal = when (log.painLevel) {
                                        PainLevel.NONE -> 1f
                                        PainLevel.MILD, PainLevel.MODERATE -> 2f
                                        PainLevel.SEVERE -> 3f
                                        else -> {
                                            // fallback check for DB string
                                            when (log.painLevel?.name?.lowercase()) {
                                                "fine" -> 1f
                                                "okay" -> 2f
                                                "severe" -> 3f
                                                else -> null
                                            }
                                        }
                                    }
                                    if (painVal != null && dayInCycle in 1..(cycle.cycleLength ?: 28)) {
                                        // map 1..3 to 0..1 scale
                                        val normalized = (painVal - 1f) / 2f
                                        dayValues.getOrPut(dayInCycle) { mutableListOf() }.add(normalized)
                                    }
                                }
                            }
                            val cycleLength = 28
                            val periodLength = 5
                            dayValues.forEach { (day, list) ->
                                val avg = list.average().toFloat()
                                val x = getNormalizedCycleX(day, periodLength, cycleLength)
                                points.add(PointF(x, avg))
                            }
                            if (points.isEmpty()) emptyList()
                            else listOf(ChartLine(label = "", points = points.sortedBy { it.x }, color = Color(0xFF9B8BC4))) // Soft Lavender
                        }
                        AnalyticsChartType.FLOW -> {
                            // Overlay up to 3 cycles as separate lines
                            val activeCycles = cycles.take(3)
                            val formatterShort = DateTimeFormatter.ofPattern("d MMM")
                            
                            activeCycles.mapIndexed { idx, cycle ->
                                val points = mutableListOf<PointF>()
                                val start = cycle.startDate
                                val end = cycle.endDate ?: start.plusDays((cycle.periodLength ?: 5).toLong() - 1)
                                
                                // Fetch logs within this period duration
                                val cycleLogs = logs.filter { !it.date.isBefore(start) && !it.date.isAfter(end) }
                                
                                cycleLogs.forEach { log ->
                                    val dayInCycle = ChronoUnit.DAYS.between(start, log.date).toInt() + 1
                                    val flowVal = when (log.flowLevel) {
                                        FlowLevel.SPOTTING, FlowLevel.LIGHT -> 1f
                                        FlowLevel.MEDIUM -> 2f
                                        FlowLevel.HEAVY -> 3f
                                        else -> null
                                    }
                                    if (flowVal != null) {
                                        val normalized = (flowVal - 1f) / 2f
                                        val x = getNormalizedCycleX(dayInCycle, cycle.periodLength ?: 5, cycle.cycleLength ?: 28)
                                        points.add(PointF(x, normalized))
                                    }
                                }

                                val label = "${start.format(formatterShort)} – ${cycle.endDate?.format(formatterShort) ?: "..."}"
                                val opacity = when (idx) {
                                    0 -> 1.0f
                                    1 -> 0.7f
                                    else -> 0.4f
                                }
                                ChartLine(label = label, points = points.sortedBy { it.x }, color = Color(0xFFC97A8A), opacity = opacity) // Rose Red
                            }.filter { it.points.isNotEmpty() }
                        }
                    }

                    val yLabels = when (chartType) {
                        AnalyticsChartType.ENERGY -> listOf("Low", "Normal", "High")
                        AnalyticsChartType.MOOD -> listOf("Low", "Neutral", "Great")
                        AnalyticsChartType.PAIN -> listOf("Fine", "Okay", "Severe")
                        AnalyticsChartType.FLOW -> listOf("Light", "Medium", "Heavy")
                    }

                    AnalyticsUiState(
                        isLoading = false,
                        selectedChartType = chartType,
                        selectedTimePeriod = period,
                        chartLines = lines,
                        yAxisLabels = yLabels,
                        isEmpty = lines.isEmpty() || lines.all { it.points.isEmpty() }
                    )
                }.getOrDefault(
                    AnalyticsUiState(
                        isLoading = false,
                        selectedChartType = chartType,
                        selectedTimePeriod = period,
                        chartLines = emptyList(),
                        isEmpty = true
                    )
                )
            }.let { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun mapMoodsToScore(moods: List<String>): Float? {
        if (moods.isEmpty()) return 3.0f // Neutral

        val scores = moods.mapNotNull { mood ->
            when (mood.lowercase()) {
                "excited", "zen", "loving" -> 5.0f
                "happy", "calm" -> 4.0f
                "moody" -> 3.0f
                "stressed", "anxious", "irritated" -> 2.0f
                "sad" -> 1.0f
                else -> null
            }
        }
        return if (scores.isNotEmpty()) scores.average().toFloat() else null
    }

    private fun getNormalizedCycleX(dayInCycle: Int, periodLength: Int, cycleLength: Int): Float {
        return when {
            dayInCycle <= periodLength -> {
                val frac = (dayInCycle - 1).toFloat() / maxOf(1, periodLength)
                0.0f + frac * 0.25f
            }
            dayInCycle <= 13 -> {
                val length = 13 - periodLength
                val frac = (dayInCycle - periodLength - 1).toFloat() / maxOf(1, length)
                0.25f + frac * 0.25f
            }
            dayInCycle <= 16 -> {
                val frac = (dayInCycle - 14).toFloat() / 3f
                0.50f + frac * 0.25f
            }
            else -> {
                val length = cycleLength - 16
                val frac = (dayInCycle - 17).toFloat() / maxOf(1, length)
                0.75f + frac * 0.25f
            }
        }.coerceIn(0f, 1f)
    }
}
