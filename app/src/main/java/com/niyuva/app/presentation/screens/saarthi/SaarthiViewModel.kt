package com.niyuva.app.presentation.screens.saarthi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.data.remote.AiRepository
import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.domain.model.ChatRole
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.model.LogSource
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.DischargeType
import com.niyuva.app.domain.model.SleepQuality
import com.niyuva.app.domain.model.LogExtraction
import com.niyuva.app.domain.repository.ChatRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.domain.usecase.GetAllChatMessagesUseCase
import com.niyuva.app.domain.usecase.GetCurrentPhaseUseCase
import com.niyuva.app.domain.usecase.PhaseResult
import com.niyuva.app.domain.usecase.SaveDailyLogUseCase
import com.niyuva.app.domain.usecase.GetLogForDateUseCase
import com.niyuva.app.domain.usecase.ExtractLogsFromMessageUseCase
import com.niyuva.app.domain.usecase.MatchSaarthiResponseUseCase
import com.niyuva.app.domain.usecase.PostProcessAiResponseUseCase
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.PhaseThemeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class SaarthiUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,  // Saarthi is generating response
    val phase: CyclePhase = CyclePhase.FOLLICULAR,
    val phaseTheme: PhaseThemeData = PhaseThemeData.fromPhase(CyclePhase.FOLLICULAR),
    val userName: String = "",
    val hasConversation: Boolean = false,  // false = show empty state, true = show chat
    val error: String? = null,
    val remainingFreeCredits: Int = 20,     // placeholder — always show 20 for Mode 1
    val showAiWarning: Boolean = false
)

@HiltViewModel
class SaarthiViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val saveDailyLogUseCase: SaveDailyLogUseCase,
    private val getCurrentPhaseUseCase: GetCurrentPhaseUseCase,
    private val userProfileRepository: UserProfileRepository,
    private val getAllChatMessagesUseCase: GetAllChatMessagesUseCase,
    private val getLogForDateUseCase: GetLogForDateUseCase,
    private val extractLogsFromMessageUseCase: ExtractLogsFromMessageUseCase,
    private val matchSaarthiResponseUseCase: MatchSaarthiResponseUseCase,
    private val aiRepository: AiRepository,
    private val postProcessAiResponseUseCase: PostProcessAiResponseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaarthiUiState())
    val uiState: StateFlow<SaarthiUiState> = _uiState.asStateFlow()

    init {
        loadSaarthiData()
    }

    private fun loadSaarthiData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Get profile/userName
            val profile = userProfileRepository.getProfile()
            val userName = profile?.name ?: ""

            // 2. Get current cycle phase
            val phaseResult = getCurrentPhaseUseCase()
            val phase = when (phaseResult) {
                is PhaseResult.Known -> phaseResult.phase
                else -> CyclePhase.FOLLICULAR
            }
            val phaseTheme = PhaseThemeData.fromPhase(phase)

            // 3. Observe chat history
            getAllChatMessagesUseCase().collect { messages ->
                _uiState.update { state ->
                    state.copy(
                        messages = messages,
                        userName = userName,
                        phase = phase,
                        phaseTheme = phaseTheme,
                        hasConversation = messages.isNotEmpty(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onSendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                // 1. Clear input, set hasConversation = true, isLoading = true
                _uiState.update { it.copy(inputText = "", hasConversation = true, isLoading = true) }

                // 2. Save user message to database
                val userMsg = ChatMessage(
                    timestamp = LocalDateTime.now(),
                    role = ChatRole.USER,
                    message = text
                )
                chatRepository.saveMessage(userMsg)

                // 3. Extract any logs from the message
                val extraction = extractLogsFromMessageUseCase(text)
                if (extraction != null) {
                    val today = LocalDate.now()
                    val existingLog = getLogForDateUseCase(today)
                    val updatedLog = if (existingLog != null) {
                        when (extraction.field) {
                            "flow_level" -> existingLog.copy(
                                flowLevel = FlowLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "pain_level" -> existingLog.copy(
                                painLevel = PainLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "energy_level" -> existingLog.copy(
                                energyLevel = EnergyLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "mood" -> {
                                val newMoods = (existingLog.moods + extraction.value).distinct()
                                existingLog.copy(
                                    moods = newMoods,
                                    source = LogSource.SAARTHI
                                )
                            }
                            "discharge_type" -> existingLog.copy(
                                dischargeType = DischargeType.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "sleep_quality" -> existingLog.copy(
                                sleepQuality = SleepQuality.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            else -> existingLog
                        }
                    } else {
                        when (extraction.field) {
                            "flow_level" -> DailyLog(
                                date = today,
                                flowLevel = FlowLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "pain_level" -> DailyLog(
                                date = today,
                                painLevel = PainLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "energy_level" -> DailyLog(
                                date = today,
                                energyLevel = EnergyLevel.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "mood" -> DailyLog(
                                date = today,
                                moods = listOf(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "discharge_type" -> DailyLog(
                                date = today,
                                dischargeType = DischargeType.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            "sleep_quality" -> DailyLog(
                                date = today,
                                sleepQuality = SleepQuality.fromString(extraction.value),
                                source = LogSource.SAARTHI
                            )
                            else -> DailyLog(date = today, source = LogSource.SAARTHI)
                        }
                    }
                    saveDailyLogUseCase(updatedLog)
                }

                // 4. Match response / call AI
                val profile = userProfileRepository.getProfile()
                val responseText = if (profile?.aiEnabled == true && profile.aiProvider != null) {
                    try {
                        val raw = aiRepository.sendMessage(text, profile.aiProvider)
                        _uiState.update { it.copy(showAiWarning = false) }
                        postProcessAiResponseUseCase(raw)
                    } catch (e: Exception) {
                        // Fallback to Mode 1 on any error
                        _uiState.update { it.copy(showAiWarning = true) }
                        matchSaarthiResponseUseCase(text)?.response ?: FALLBACK_RESPONSE
                    }
                } else {
                    // Mode 1
                    delay(800)  // simulate "thinking" time
                    _uiState.update { it.copy(showAiWarning = false) }
                    matchSaarthiResponseUseCase(text)?.response ?: FALLBACK_RESPONSE
                }

                // 5. If extraction detected, append confirmation to response
                val finalResponse = if (extraction != null) {
                    "$responseText\n\n✅ Note kar liya: ${extractionConfirmation(extraction)}"
                } else responseText

                // 6. Save Saarthi response to database
                val saarthiMsg = ChatMessage(
                    timestamp = LocalDateTime.now(),
                    role = ChatRole.SAARTHI,
                    message = finalResponse,
                    logsExtracted = extraction?.let { mapOf(it.field to it.value) }
                )
                chatRepository.saveMessage(saarthiMsg)

                // 7. Update state
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ek baar phir try karo behna — network thoda slow lag raha hai 💛"
                    )
                }
            }
        }
    }

    fun onQuickQuestionTapped(question: String) {
        _uiState.update { it.copy(inputText = question) }
        onSendMessage()
    }

    fun clearConversation() {
        viewModelScope.launch {
            try {
                chatRepository.clearChat()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Chat clear nahi ho payi 😢") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun extractionConfirmation(extraction: LogExtraction): String = when (extraction.field) {
        "flow_level"   -> "Flow ${extraction.value} tha aaj 🌸"
        "pain_level"   -> "Pain level ${extraction.value} note kiya 💛"
        "energy_level" -> "Energy ${extraction.value} thi aaj ⚡"
        "mood"         -> "Mood ${extraction.value} — logged 🌸"
        else           -> "${extraction.field}: ${extraction.value}"
    }

    companion object {
        private const val FALLBACK_RESPONSE = "Arre behna, yeh sawaal thoda alag hai jo main abhi directly answer nahi kar sakti 😊 Agar AI enable karo settings mein, toh main aur gehrai se baat kar sakti hoon. Warna mujhse period, hormones, discharge ya koi bhi body sawaal poochho — didi hoon teri, sab bataungi 💛"
    }
}
