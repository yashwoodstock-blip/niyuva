package com.niyuva.app.presentation.theme

import androidx.compose.ui.graphics.Color

enum class CyclePhase { MENSTRUATION, FOLLICULAR, OVULATION, LUTEAL }

data class PhaseThemeData(
    val phase: CyclePhase,
    val backgroundGradientStart: Color,
    val backgroundGradientEnd: Color,
    val ringColor: Color,
    val cardTint: Color,
    val tipCardColor: Color,
    val greetingText: String,
    val phaseName: String,
    val phaseMoodEmoji: String
) {
    companion object {
        // Named defaults for easy reference before instances are defined
        val MENSTRUATION: PhaseThemeData get() = MenstruationTheme
        val FOLLICULAR: PhaseThemeData get() = FollicularTheme
        val OVULATION: PhaseThemeData get() = OvulationTheme
        val LUTEAL: PhaseThemeData get() = LutealTheme

        fun fromPhase(phase: CyclePhase): PhaseThemeData = when (phase) {
            CyclePhase.MENSTRUATION -> MenstruationTheme
            CyclePhase.FOLLICULAR   -> FollicularTheme
            CyclePhase.OVULATION    -> OvulationTheme
            CyclePhase.LUTEAL       -> LutealTheme
        }
    }
}

val MenstruationTheme = PhaseThemeData(
    phase = CyclePhase.MENSTRUATION,
    backgroundGradientStart = PhaseColorMenstruation,
    backgroundGradientEnd = PhaseGradientMenstruationEnd,
    ringColor = PhaseColorMenstruation,
    cardTint = PhaseColorMenstruation,
    tipCardColor = PhaseGradientMenstruationEnd,
    greetingText = "Rest and nurture yourself.",
    phaseName = "Menstruation",
    phaseMoodEmoji = "🌸"
)

val FollicularTheme = PhaseThemeData(
    phase = CyclePhase.FOLLICULAR,
    backgroundGradientStart = PhaseColorFollicular,
    backgroundGradientEnd = PhaseGradientFollicularEnd,
    ringColor = PhaseColorFollicular,
    cardTint = PhaseColorFollicular,
    tipCardColor = PhaseGradientFollicularEnd,
    greetingText = "Feel your energy rising.",
    phaseName = "Follicular",
    phaseMoodEmoji = "✨"
)

val OvulationTheme = PhaseThemeData(
    phase = CyclePhase.OVULATION,
    backgroundGradientStart = PhaseColorOvulation,
    backgroundGradientEnd = PhaseGradientOvulationEnd,
    ringColor = PhaseColorOvulation,
    cardTint = PhaseColorOvulation,
    tipCardColor = PhaseGradientOvulationEnd,
    greetingText = "You are glowing and social.",
    phaseName = "Ovulation",
    phaseMoodEmoji = "☀️"
)

val LutealTheme = PhaseThemeData(
    phase = CyclePhase.LUTEAL,
    backgroundGradientStart = PhaseColorLuteal,
    backgroundGradientEnd = PhaseGradientLutealEnd,
    ringColor = PhaseColorLuteal,
    cardTint = PhaseColorLuteal,
    tipCardColor = PhaseGradientLutealEnd,
    greetingText = "Slow down and turn inward.",
    phaseName = "Luteal",
    phaseMoodEmoji = "🌙"
)
