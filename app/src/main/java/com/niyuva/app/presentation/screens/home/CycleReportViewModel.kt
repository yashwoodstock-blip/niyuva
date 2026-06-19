package com.niyuva.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.CyclePrediction
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.usecase.CalculatePredictionsUseCase
import com.niyuva.app.domain.usecase.GetRecentCyclesUseCase
import com.niyuva.app.domain.usecase.GetUserProfileUseCase
import com.niyuva.app.domain.usecase.GetLogsInRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

enum class RegularityStatus { REGULAR, IRREGULAR, UNKNOWN }

data class CycleReportUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val cycles: List<Cycle> = emptyList(),
    val prediction: CyclePrediction? = null,
    val averageCycleLength: Int? = null,
    val averagePeriodLength: Int? = null,
    val regularityStatus: RegularityStatus = RegularityStatus.UNKNOWN,
    val lastPeriodStart: LocalDate? = null,
    val lastPeriodEnd: LocalDate? = null,
    val lastPeriodFlow: String = "—"
)

@HiltViewModel
class CycleReportViewModel @Inject constructor(
    private val getRecentCyclesUseCase: GetRecentCyclesUseCase,
    private val calculatePredictionsUseCase: CalculatePredictionsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getLogsInRangeUseCase: GetLogsInRangeUseCase,
    private val doctorReportGenerator: com.niyuva.app.domain.usecase.DoctorReportGenerator
) : ViewModel() {

    fun generateDoctorReport(name: String, months: Int, onComplete: (java.io.File?) -> Unit) {
        viewModelScope.launch {
            val file = doctorReportGenerator.generateReport(name, months)
            onComplete(file)
        }
    }

    fun getShareUri(file: java.io.File): android.net.Uri {
        return doctorReportGenerator.getShareUri(file)
    }

    private val _uiState = MutableStateFlow(CycleReportUiState())
    val uiState: StateFlow<CycleReportUiState> = _uiState.asStateFlow()

    init {
        loadReportData()
    }

    fun loadReportData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            withContext(Dispatchers.IO) {
                runCatching {
                    // 1. Load User Profile
                    val profile = getUserProfileUseCase.getProfile()
                    val userName = profile?.name?.takeIf { it.isNotBlank() } ?: ""

                    // 2. Load recent 6 cycles
                    val cycles = getRecentCyclesUseCase(count = 6)

                    // 3. Load Predictions
                    val prediction = runCatching {
                        calculatePredictionsUseCase(LocalDate.now())
                    }.getOrNull()

                    // Calculate Averages
                    val validCycleLengths = cycles.mapNotNull { it.cycleLength }
                    val averageCycle = if (validCycleLengths.isNotEmpty()) validCycleLengths.average().toInt() else profile?.averageCycleLength

                    val validPeriodLengths = cycles.mapNotNull { it.periodLength }
                    val averagePeriod = if (validPeriodLengths.isNotEmpty()) validPeriodLengths.average().toInt() else profile?.averagePeriodLength

                    // Regularity calculation from last 3 cycles
                    val last3Lengths = cycles.take(3).mapNotNull { it.cycleLength }
                    val regularity = if (last3Lengths.size < 3) {
                        RegularityStatus.UNKNOWN
                    } else {
                        val max = last3Lengths.maxOrNull() ?: 0
                        val min = last3Lengths.minOrNull() ?: 0
                        if (max - min <= 5) RegularityStatus.REGULAR else RegularityStatus.IRREGULAR
                    }

                    // Last period info from the latest cycle
                    val latestCycle = cycles.firstOrNull()
                    val lastStart = latestCycle?.startDate
                    val lastEnd = latestCycle?.endDate
                    var flowLevelStr = "—"

                    if (latestCycle != null) {
                        val rangeStart = latestCycle.startDate
                        val rangeEnd = latestCycle.endDate ?: latestCycle.startDate.plusDays((latestCycle.periodLength ?: 5).toLong() - 1)
                        val logs = getLogsInRangeUseCase(rangeStart, rangeEnd)
                        val maxFlow = logs.mapNotNull { it.flowLevel }
                            .maxByOrNull {
                                when (it) {
                                    FlowLevel.HEAVY -> 4
                                    FlowLevel.MEDIUM -> 3
                                    FlowLevel.LIGHT -> 2
                                    FlowLevel.SPOTTING -> 1
                                }
                            }
                        if (maxFlow != null) {
                            flowLevelStr = maxFlow.name.lowercase()
                        }
                    }

                    CycleReportUiState(
                        isLoading = false,
                        userName = userName,
                        cycles = cycles,
                        prediction = prediction,
                        averageCycleLength = averageCycle,
                        averagePeriodLength = averagePeriod,
                        regularityStatus = regularity,
                        lastPeriodStart = lastStart,
                        lastPeriodEnd = lastEnd,
                        lastPeriodFlow = flowLevelStr
                    )
                }.onSuccess { state ->
                    _uiState.value = state
                }.onFailure {
                    _uiState.value = CycleReportUiState(
                        isLoading = false,
                        userName = "",
                        cycles = emptyList(),
                        prediction = null,
                        averageCycleLength = null,
                        averagePeriodLength = null,
                        regularityStatus = RegularityStatus.UNKNOWN,
                        lastPeriodStart = null,
                        lastPeriodEnd = null,
                        lastPeriodFlow = "—"
                    )
                }
            }
        }
    }
}
