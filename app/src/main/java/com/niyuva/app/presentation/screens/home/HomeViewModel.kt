package com.niyuva.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.BloodColor
import com.niyuva.app.domain.model.ClotSize
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.model.DischargeType
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.model.SleepQuality
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.InsightRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.domain.repository.StreakRepository
import com.niyuva.app.domain.usecase.BuildDayStripUseCase
import com.niyuva.app.domain.usecase.CalculatePredictionsUseCase
import com.niyuva.app.domain.usecase.GetCurrentPhaseUseCase
import com.niyuva.app.domain.usecase.GetDailyTipUseCase
import com.niyuva.app.domain.usecase.GetLogForDateUseCase
import com.niyuva.app.domain.usecase.PhaseResult
import com.niyuva.app.domain.usecase.SaveDailyLogUseCase
import com.niyuva.app.domain.usecase.ScheduleAllNotificationsUseCase
import com.niyuva.app.domain.usecase.StreakTracker
import com.niyuva.app.domain.usecase.StreakResult
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.PhaseThemeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentPhaseUseCase: GetCurrentPhaseUseCase,
    private val calculatePredictionsUseCase: CalculatePredictionsUseCase,
    private val buildDayStripUseCase: BuildDayStripUseCase,
    private val getDailyTipUseCase: GetDailyTipUseCase,
    private val getLogForDateUseCase: GetLogForDateUseCase,
    private val saveDailyLogUseCase: SaveDailyLogUseCase,
    private val userProfileRepository: UserProfileRepository,
    private val insightRepository: InsightRepository,
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase,
    private val cycleRepository: CycleRepository,
    private val streakRepository: StreakRepository,
    private val streakTracker: StreakTracker
) : ViewModel() {

    // ─────────────────────────────────────────────
    // State
    // ─────────────────────────────────────────────

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Log sheet
    private val _logSheetState = MutableStateFlow(LogSheetState())
    val logSheetState: StateFlow<LogSheetState> = _logSheetState.asStateFlow()

    private val _showLogSheet = MutableStateFlow(false)
    val showLogSheet: StateFlow<Boolean> = _showLogSheet.asStateFlow()

    private var isIrregularityCardDismissed = false
    private var recoveryPromptShownThisSession = false

    // ─────────────────────────────────────────────
    // Initialization
    // ─────────────────────────────────────────────

    init {
        loadHomeData()
    }

    // ─────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────

    /** Re-loads all home data (e.g. called on pull-to-refresh or app resume). */
    fun refreshHomeData() {
        loadHomeData()
    }

    private fun getSaarthiLoggedFields(log: DailyLog): Set<OptionalLogCategory> {
        val set = mutableSetOf<OptionalLogCategory>()
        if (log.source == com.niyuva.app.domain.model.LogSource.SAARTHI) {
            if (log.energyLevel != null) set.add(OptionalLogCategory.ENERGY)
            if (log.dischargeType != null) set.add(OptionalLogCategory.DISCHARGE)
            if (log.sleepQuality != null) set.add(OptionalLogCategory.SLEEP)
            if (log.moods.isNotEmpty()) set.add(OptionalLogCategory.MOOD)
        }
        return set
    }

    // ─────────────────────────────────────────────
    // Log Sheet public API
    // ─────────────────────────────────────────────

    /** Opens the log symptoms sheet and resets sheet state to today, loading any existing log. */
    fun showLogSheet() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val existing = withContext(Dispatchers.IO) {
                runCatching { getLogForDateUseCase(today) }.getOrNull()
            }
            _logSheetState.value = if (existing != null) {
                LogSheetState(
                    selectedDate = today,
                    flowLevel = existing.flowLevel,
                    bloodColor = existing.bloodColor,
                    clotSize = existing.clotSize,
                    painLevel = existing.painLevel,
                    painTypes = existing.painTypes.toSet(),
                    dischargeType = existing.dischargeType,
                    energyLevel = existing.energyLevel,
                    sleepQuality = existing.sleepQuality,
                    moods = existing.moods.toSet(),
                    birthControl = existing.birthControl,
                    saarthiLoggedToday = getSaarthiLoggedFields(existing)
                )
            } else {
                LogSheetState(selectedDate = today)
            }
            _showLogSheet.value = true
        }
    }

    fun toggleSnapshotRowExpanded() {
        _uiState.value = _uiState.value.copy(
            isSnapshotRowExpanded = !_uiState.value.isSnapshotRowExpanded
        )
    }

    fun dismissRecoveryPrompt(applyReset: Boolean) {
        recoveryPromptShownThisSession = true
        _uiState.value = _uiState.value.copy(showRecoveryPromptCard = false)
        if (applyReset) {
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    streakTracker.resetStreakTo1()
                }
                loadHomeData()
            }
        }
    }

    fun showLogSheetFromRecovery() {
        recoveryPromptShownThisSession = true
        _uiState.value = _uiState.value.copy(showRecoveryPromptCard = false)
        viewModelScope.launch {
            val today = LocalDate.now()
            val existing = withContext(Dispatchers.IO) {
                runCatching { getLogForDateUseCase(today) }.getOrNull()
            }
            _logSheetState.value = if (existing != null) {
                LogSheetState(
                    selectedDate = today,
                    flowLevel = existing.flowLevel,
                    bloodColor = existing.bloodColor,
                    clotSize = existing.clotSize,
                    painLevel = existing.painLevel,
                    painTypes = existing.painTypes.toSet(),
                    dischargeType = existing.dischargeType,
                    energyLevel = existing.energyLevel,
                    sleepQuality = existing.sleepQuality,
                    moods = existing.moods.toSet(),
                    birthControl = existing.birthControl,
                    continueStreak = true,
                    saarthiLoggedToday = getSaarthiLoggedFields(existing)
                )
            } else {
                LogSheetState(
                    selectedDate = today,
                    continueStreak = true
                )
            }
            _showLogSheet.value = true
        }
    }

    /** Closes the log symptoms sheet without saving. */
    fun hideLogSheet() {
        _showLogSheet.value = false
    }

    fun onFlowSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            flowLevel = key?.let { FlowLevel.fromString(it) }
        )
    }

    fun onBloodColorSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            bloodColor = key?.let { BloodColor.fromString(it) }
        )
    }

    fun onClotSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            clotSize = key?.let { ClotSize.fromString(it) }
        )
    }

    fun onPainLevelSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            painLevel = key?.let { PainLevel.fromString(it) },
            painTypes = emptySet() // reset types when intensity changes
        )
    }

    fun onPainTypeToggled(key: String) {
        val current = _logSheetState.value.painTypes
        _logSheetState.value = _logSheetState.value.copy(
            painTypes = if (key in current) current - key else current + key
        )
    }

    fun onMoodToggled(key: String) {
        val current = _logSheetState.value.moods
        _logSheetState.value = _logSheetState.value.copy(
            moods = if (key in current) current - key else current + key
        )
    }

    fun onSleepQualitySelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            sleepQuality = key?.let { SleepQuality.fromString(it) }
        )
    }

    fun onEnergyLevelSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            energyLevel = key?.let { EnergyLevel.fromString(it) }
        )
    }

    fun onDischargeTypeSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            dischargeType = key?.let { DischargeType.fromString(it) }
        )
    }



    fun onBirthControlSelected(key: String?) {
        _logSheetState.value = _logSheetState.value.copy(
            birthControl = key
        )
    }

    /**
     * Saves the current [LogSheetState] as a [DailyLog] via [SaveDailyLogUseCase].
     * Runs on [Dispatchers.IO]. Refreshes home data on success and closes the sheet.
     */
    fun saveLog() {
        viewModelScope.launch {
            _logSheetState.value = _logSheetState.value.copy(isSaving = true, hasError = false)
            val sheet = _logSheetState.value
            var streakResult: StreakResult = StreakResult.NoChange
            withContext(Dispatchers.IO) {
                runCatching {
                    saveDailyLogUseCase(
                        DailyLog(
                            date          = sheet.selectedDate,
                            flowLevel     = sheet.flowLevel,
                            bloodColor    = sheet.bloodColor,
                            clotSize      = sheet.clotSize,
                            painLevel     = sheet.painLevel,
                            painTypes     = sheet.painTypes.toList(),
                            dischargeType = sheet.dischargeType,
                            energyLevel   = sheet.energyLevel,
                            sleepQuality  = sheet.sleepQuality,
                            moods         = sheet.moods.toList(),
                            sexualActivity= null,
                            birthControl  = sheet.birthControl
                        ),
                        continueStreak = sheet.continueStreak
                    )
                }
            }.onSuccess { result ->
                streakResult = result
                _logSheetState.value = _logSheetState.value.copy(
                    isSaving     = false,
                    savedSuccess = true,
                    hasError     = false
                )
                // Wait 800ms before closing the sheet
                kotlinx.coroutines.delay(800)
                hideLogSheet()
                loadHomeData() // refresh home after logging

                // Process StreakResult
                when (streakResult) {
                    is StreakResult.StreakResetTo1 -> {
                        _uiState.value = _uiState.value.copy(showMissedDaySnackbar = true)
                        viewModelScope.launch {
                            kotlinx.coroutines.delay(3000)
                            _uiState.value = _uiState.value.copy(showMissedDaySnackbar = false)
                        }
                    }
                    is StreakResult.MilestoneReached -> {
                        _uiState.value = _uiState.value.copy(streakMilestoneCelebration = (streakResult as StreakResult.MilestoneReached).milestone)
                    }
                    else -> {}
                }

                // Wait another 1200ms (total 2s success display time on home screen) before resetting state
                kotlinx.coroutines.delay(1200)
                _logSheetState.value = LogSheetState()
            }.onFailure {
                _logSheetState.value = _logSheetState.value.copy(
                    isSaving = false,
                    hasError = true
                )
            }
        }
    }

    /** Dismisses the milestone celebration overlay and marks it shown in the database. */
    fun dismissMilestoneCelebration() {
        val currentMilestone = _uiState.value.streakMilestoneCelebration
        if (currentMilestone != null) {
            viewModelScope.launch(Dispatchers.IO) {
                streakTracker.markMilestoneShown(currentMilestone)
                _uiState.value = _uiState.value.copy(streakMilestoneCelebration = null)
            }
        }
    }

    /** Dismisses the missed day recovery snackbar. */
    fun dismissMissedDaySnackbar() {
        _uiState.value = _uiState.value.copy(showMissedDaySnackbar = false)
    }

    /**
     * No-op in the ViewModel — signals the UI layer to scroll the strip to “today”.
     */
    fun onTodayDayStripTapped() {
        // Intentional no-op — scroll command is delegated to the UI layer.
    }

    /** Dismisses the irregularity card in-memory for the current session. */
    fun dismissIrregularityCard() {
        isIrregularityCardDismissed = true
        _uiState.value = _uiState.value.copy(showIrregularityCard = false)
    }

    // ─────────────────────────────────────────────
    // Data loading
    // ─────────────────────────────────────────────

    private fun loadHomeData() {
        viewModelScope.launch {
            // Ensure we are always in loading state at the start of a refresh
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Hard time-out: never stay in loading state for more than 2 seconds
            val result = withTimeoutOrNull(2_000L) {
                withContext(Dispatchers.IO) {
                    runCatching { fetchAllData() }
                }
            }

            when {
                result == null -> {
                    // Timeout — surface an error but keep any previously loaded data intact
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Data load timed out. Please try again."
                    )
                }
                result.isFailure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage
                            ?: "Something went wrong. Please try again."
                    )
                }
                result.isSuccess -> {
                    val newState = result.getOrThrow()
                    
                    // Reschedule notifications since cycle dates/predictions might have updated
                    runCatching { scheduleAllNotificationsUseCase() }

                    // Skip re-emission if the phase hasn't changed (avoid unnecessary recompositions)
                    val currentState = _uiState.value
                    if (!currentState.isLoading &&
                        currentState.currentPhase == newState.currentPhase &&
                        currentState.dayStrip == newState.dayStrip &&
                        currentState.hasLoggedToday == newState.hasLoggedToday &&
                        currentState.showIrregularityCard == newState.showIrregularityCard
                    ) return@launch

                    _uiState.value = newState
                }
            }
        }
    }

    /**
     * Performs all repository/use-case calls and assembles a fresh [HomeUiState].
     * Must be called from an IO-aware context.
     */
    private suspend fun fetchAllData(): HomeUiState {
        val today = LocalDate.now()

        // 1. User profile
        val profile = userProfileRepository.getProfile()
        val name = profile?.name?.takeIf { it.isNotBlank() } ?: ""
        val defaultPeriodLength = profile?.averagePeriodLength ?: 5

        // Check if no cycle is logged
        val cycleCount = cycleRepository.getCycleCount()
        val lastPeriodStart = profile?.lastPeriodStartDate
        val showEmptyState = cycleCount == 0 && lastPeriodStart == null

        // 2. Current phase
        val phaseResult = getCurrentPhaseUseCase()
        val (phase, dayInCycle, totalCycleDays) = when (phaseResult) {
            is PhaseResult.Known -> Triple(
                phaseResult.phase,
                phaseResult.dayInCycle,
                phaseResult.totalCycleDays
            )
            PhaseResult.Unknown -> Triple(CyclePhase.FOLLICULAR, 1, 28)
        }

        // 3. Theme resolution — pure data, no UI imports
        val phaseTheme = PhaseThemeData.fromPhase(phase)

        // 4. Prediction (non-fatal: catch and surface inline)
        val prediction = runCatching {
            calculatePredictionsUseCase(today)
        }.getOrNull()

        // 5. Day strip
        val dayStrip = buildDayStripUseCase(today, defaultPeriodLength)

        // 6. Daily tip
        val todayTip = getDailyTipUseCase(phase, dayInCycle)

        // 7. Today's log
        val todayLog = getLogForDateUseCase(today)

        // 8. Greeting
        val greeting = buildGreeting(name, phase)

        // 9. Irregularity warning card
        val latestInsight = runCatching {
            insightRepository.getLatestInsightByType("irregularity_flag")
        }.getOrNull()

        val showIrregularityCard = if (isIrregularityCardDismissed) {
            false
        } else {
            latestInsight != null && latestInsight.date.isAfter(today.minusDays(30))
        }

        val dayInPhase = when (phase) {
            CyclePhase.MENSTRUATION -> dayInCycle
            CyclePhase.FOLLICULAR   -> dayInCycle - defaultPeriodLength
            CyclePhase.OVULATION    -> dayInCycle - 13
            CyclePhase.LUTEAL       -> dayInCycle - 16
        }.coerceAtLeast(1)

        val streak = streakRepository.getStreak()
        val aiEnabled = profile?.aiEnabled ?: false

        val irregularityFlag = latestInsight != null
        val confidenceLevel = com.niyuva.app.domain.model.computeConfidenceLevel(
            loggedCycleCount = cycleCount,
            isAiEnabled = aiEnabled,
            irregularityFlag = irregularityFlag
        )

        val daysMissed = if (streak?.lastLogDate != null) {
            val lastLog = LocalDate.parse(streak.lastLogDate)
            java.time.temporal.ChronoUnit.DAYS.between(lastLog, today).toInt() - 1
        } else {
            -1
        }
        val showRecoveryPromptCard = streak != null
                && streak.currentStreak >= 7
                && daysMissed == 1
                && !recoveryPromptShownThisSession

        return HomeUiState(
            isLoading         = false,
            userName          = name,
            currentPhase      = phase,
            currentDayInCycle = dayInCycle,
            dayInPhase        = dayInPhase,
            totalCycleDays    = totalCycleDays,
            phaseTheme        = phaseTheme,
            prediction        = prediction,
            todayLog          = todayLog,
            hasLoggedToday    = todayLog != null,
            todayTip          = todayTip,
            greeting          = greeting,
            dayStrip          = dayStrip,
            error             = null,
            showIrregularityCard = showIrregularityCard,
            showEmptyState    = showEmptyState,
            currentStreak     = streak?.currentStreak ?: 0,
            longestStreak     = streak?.longestStreak ?: 0,
            aiEnabled         = aiEnabled,
            confidenceLevel   = confidenceLevel,
            irregularityFlag  = irregularityFlag,
            showRecoveryPromptCard = showRecoveryPromptCard,
            isSnapshotRowExpanded = _uiState.value.isSnapshotRowExpanded
        )
    }

    // ─────────────────────────────────────────────
    // Greeting helper
    // ─────────────────────────────────────────────

    private fun buildGreeting(name: String, phase: CyclePhase): String {
        val phaseGreeting = when (phase) {
            CyclePhase.MENSTRUATION -> "Rest karo — tera body kaam kar rahi hai 🌸"
            CyclePhase.FOLLICULAR   -> "Energy aa rahi hai — naya kuch try karo ✨"
            CyclePhase.OVULATION    -> "Aaj tu apne best pe hai 🔥"
            CyclePhase.LUTEAL       -> "Apne aap ke saath gentle reh 💛"
        }
        return if (name.isNotBlank()) "$name, $phaseGreeting" else phaseGreeting
    }
}
