package com.niyuva.app.domain.model

import java.time.LocalDate

data class DailyLog(
    val id: Long = 0,
    val date: LocalDate,
    val flowLevel: FlowLevel? = null,
    val bloodColor: BloodColor? = null,
    val clotSize: ClotSize? = null,
    val painLevel: PainLevel? = null,
    val painTypes: List<String> = emptyList(),
    val dischargeType: DischargeType? = null,
    val energyLevel: EnergyLevel? = null,
    val sleepQuality: SleepQuality? = null,
    val moods: List<String> = emptyList(),
    val sexualActivity: String? = null,
    val birthControl: String? = null,
    val source: LogSource = LogSource.MANUAL
)

enum class FlowLevel {
    HEAVY, MEDIUM, LIGHT, SPOTTING;

    companion object {
        fun fromString(value: String?): FlowLevel? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class BloodColor {
    PINK, BRIGHT_RED, DARK_RED, ORANGE, BROWN;

    companion object {
        fun fromString(value: String?): BloodColor? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class ClotSize {
    NONE, SMALL, MEDIUM, LARGE;

    companion object {
        fun fromString(value: String?): ClotSize? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class PainLevel {
    NONE, MILD, MODERATE, SEVERE;

    companion object {
        fun fromString(value: String?): PainLevel? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class DischargeType {
    DRY, CLOUDY, WATERY, STRETCHY, CREAMY;

    companion object {
        fun fromString(value: String?): DischargeType? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class EnergyLevel {
    LOW, NORMAL, HIGH;

    companion object {
        fun fromString(value: String?): EnergyLevel? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class SleepQuality {
    ABOVE_9HR, SIX_9HR, THREE_6HR, ZERO_3HR;

    fun toDbString(): String {
        return when (this) {
            ABOVE_9HR -> "above_9hr"
            SIX_9HR -> "6_9hr"
            THREE_6HR -> "3_6hr"
            ZERO_3HR -> "0_3hr"
        }
    }

    companion object {
        fun fromString(value: String?): SleepQuality? {
            if (value == null) return null
            return when (value.lowercase()) {
                "above_9hr" -> ABOVE_9HR
                "6_9hr", "six_9hr" -> SIX_9HR
                "3_6hr", "three_6hr" -> THREE_6HR
                "0_3hr", "zero_3hr" -> ZERO_3HR
                else -> null
            }
        }
    }
}

enum class LogSource {
    MANUAL, SAARTHI;

    companion object {
        fun fromString(value: String?): LogSource {
            if (value == null) return MANUAL
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MANUAL
        }
    }
}
