package com.niyuva.app.presentation.screens.home

import com.niyuva.app.domain.model.CyclePrediction
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.PhaseThemeData
import java.time.LocalDate

// ─────────────────────────────────────────────
// Home screen state — single source of truth
// ─────────────────────────────────────────────

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val currentPhase: CyclePhase = CyclePhase.MENSTRUATION,
    val currentDayInCycle: Int = 1,
    val dayInPhase: Int = 1,
    val totalCycleDays: Int = 28,
    /** Resolved from CyclePhase via PhaseThemeData.fromPhase() — drives all theming. */
    val phaseTheme: PhaseThemeData = PhaseThemeData.MENSTRUATION,
    val prediction: CyclePrediction? = null,
    val todayLog: DailyLog? = null,
    val hasLoggedToday: Boolean = false,
    val todayTip: DailyTip = DailyTip.default(),
    val greeting: String = "",
    val dayStrip: List<DayStripItem> = emptyList(),
    val error: String? = null,
    val showIrregularityCard: Boolean = false,
    val showEmptyState: Boolean = false,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val streakMilestoneCelebration: Int? = null,
    val showMissedDaySnackbar: Boolean = false,
    val aiEnabled: Boolean = false,
    val confidenceLevel: com.niyuva.app.domain.model.ConfidenceLevel = com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED,
    val irregularityFlag: Boolean = false,
    val isSnapshotRowExpanded: Boolean = false,
    val showRecoveryPromptCard: Boolean = false
)

// ─────────────────────────────────────────────
// 7-day horizontal calendar strip
// ─────────────────────────────────────────────

data class DayStripItem(
    val date: LocalDate,
    /** Single letter abbreviation: "M", "T", "W", "T", "F", "S", "S" */
    val dayLetter: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isLogged: Boolean,
    val isPeriodDay: Boolean,
    val isFutureDay: Boolean
)

// ─────────────────────────────────────────────
// Daily cycle tip
// ─────────────────────────────────────────────

data class DailyTip(
    val tipText: String,
    val phase: CyclePhase,
    /** Emoji string e.g. "🌸" */
    val icon: String,
    val category: String = "Nutrition 🥗"
) {
    companion object {
        fun default() = DailyTip(
            tipText = "Apne aap ka khayal rakho aaj 💛",
            phase = CyclePhase.FOLLICULAR,
            icon = "🌸",
            category = "Nutrition 🥗"
        )
    }
}
