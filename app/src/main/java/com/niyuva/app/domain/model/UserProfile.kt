package com.niyuva.app.domain.model

import java.time.LocalDate

data class UserProfile(
    val id: Int = 1,
    val name: String = "",
    val age: Int? = null,
    val averageCycleLength: Int? = null,
    val averagePeriodLength: Int? = null,
    val lastPeriodStartDate: LocalDate? = null,
    val pinHash: String? = null,
    val securityQuestion: String? = null,
    val securityAnswerHash: String? = null,
    val aiEnabled: Boolean = false,
    val aiProvider: AiProvider? = null,
    val onboardingComplete: Boolean = false
)

enum class AiProvider {
    GEMINI, GROQ, OPENROUTER;

    companion object {
        fun fromString(value: String?): AiProvider? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
