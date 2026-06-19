package com.niyuva.app.presentation.screens.home

import com.niyuva.app.domain.model.BloodColor
import com.niyuva.app.domain.model.ClotSize
import com.niyuva.app.domain.model.DischargeType
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.model.SleepQuality
import java.time.LocalDate

/**
 * Ephemeral state for the Log Symptoms bottom sheet.
 *
 * Every nullable field represents an unset selection — the user has not yet picked that value.
 * Sets ([painTypes], [moods]) support multi-select.
 */
enum class OptionalLogCategory { ENERGY, DISCHARGE, SLEEP, MOOD }

data class LogSheetState(
    val selectedDate: LocalDate = LocalDate.now(),
    val flowLevel: FlowLevel? = null,
    val bloodColor: BloodColor? = null,
    val clotSize: ClotSize? = null,
    val painLevel: PainLevel? = null,
    val painTypes: Set<String> = emptySet(),
    val dischargeType: DischargeType? = null,
    val energyLevel: EnergyLevel? = null,
    val sleepQuality: SleepQuality? = null,
    val moods: Set<String> = emptySet(),
    val birthControl: String? = null,
    val isSaving: Boolean = false,
    val savedSuccess: Boolean = false,
    val hasError: Boolean = false,
    val continueStreak: Boolean = false,
    val saarthiLoggedToday: Set<OptionalLogCategory> = emptySet()
)
