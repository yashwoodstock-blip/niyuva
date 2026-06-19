package com.niyuva.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.repository.AiAnalysisRepository
import com.niyuva.app.domain.usecase.HealthAnalysisEngine
import com.niyuva.app.domain.repository.CycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalysisResultsUiState(
    val isLoading: Boolean = false,
    val progressMessage: String = "",
    val activeChunkIndex: Int = 0, // 0 = done/idle, 1, 2, 3 = running chunk
    val cyclePatterns: String? = null,
    val symptomsCorrelations: String? = null,
    val synthesisSummary: String? = null,
    val error: String? = null,
    val cycleCount: Int = 0
)

@HiltViewModel
class AnalysisResultsViewModel @Inject constructor(
    private val healthAnalysisEngine: HealthAnalysisEngine,
    private val aiAnalysisRepository: AiAnalysisRepository,
    private val cycleRepository: CycleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisResultsUiState())
    val uiState: StateFlow<AnalysisResultsUiState> = _uiState.asStateFlow()

    init {
        loadExistingAnalysis()
    }

    fun loadExistingAnalysis() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val count = cycleRepository.getCycleCount()
            val results = aiAnalysisRepository.getAllResults()
            val chunk1 = results.find { it.chunkSequence == 1 }?.parsedSummary
            val chunk2 = results.find { it.chunkSequence == 2 }?.parsedSummary
            val chunk3 = results.find { it.chunkSequence == 3 }?.parsedSummary

            if (chunk1 != null && chunk2 != null && chunk3 != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeChunkIndex = 0,
                        cyclePatterns = chunk1,
                        symptomsCorrelations = chunk2,
                        synthesisSummary = chunk3,
                        cycleCount = count
                    )
                }
            } else {
                runNewAnalysis()
            }
        }
    }

    fun runNewAnalysis() {
        viewModelScope.launch {
            val count = cycleRepository.getCycleCount()
            _uiState.update {
                it.copy(
                    isLoading = true,
                    progressMessage = "Analysis shuru ho raha hai...",
                    activeChunkIndex = 1,
                    cyclePatterns = null,
                    symptomsCorrelations = null,
                    synthesisSummary = null,
                    error = null,
                    cycleCount = count
                )
            }

            val success = healthAnalysisEngine.runAnalysis { chunkIndex, statusMsg ->
                viewModelScope.launch {
                    val results = aiAnalysisRepository.getAllResults()
                    val c1 = results.find { it.chunkSequence == 1 }?.parsedSummary
                    val c2 = results.find { it.chunkSequence == 2 }?.parsedSummary
                    val c3 = results.find { it.chunkSequence == 3 }?.parsedSummary

                    _uiState.update { state ->
                        state.copy(
                            progressMessage = statusMsg,
                            activeChunkIndex = if (chunkIndex == 0) 0 else chunkIndex,
                            cyclePatterns = c1,
                            symptomsCorrelations = c2,
                            synthesisSummary = c3,
                            cycleCount = count,
                            error = if (chunkIndex == 0 && statusMsg.startsWith("Error")) statusMsg else null
                        )
                    }
                }
            }

            if (success) {
                val results = aiAnalysisRepository.getAllResults()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeChunkIndex = 0,
                        progressMessage = "",
                        cyclePatterns = results.find { r -> r.chunkSequence == 1 }?.parsedSummary,
                        symptomsCorrelations = results.find { r -> r.chunkSequence == 2 }?.parsedSummary,
                        synthesisSummary = results.find { r -> r.chunkSequence == 3 }?.parsedSummary,
                        cycleCount = count
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeChunkIndex = 0,
                        cycleCount = count
                    )
                }
            }
        }
    }
}
