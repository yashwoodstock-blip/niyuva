package com.niyuva.app.domain.usecase

import com.niyuva.app.data.local.content.DidiResponseLibrary
import com.niyuva.app.domain.model.DidiResponse
import javax.inject.Inject

class MatchDidiResponseUseCase @Inject constructor(
    private val library: DidiResponseLibrary
) {
    operator fun invoke(userMessage: String): DidiResponse? {
        val normalized = userMessage.lowercase().trim()

        // Priority: doctor/serious keywords first (safety)
        val doctorResponse = library.responses.firstOrNull { it.id == "doctor_recommendation" }
        if (doctorResponse != null && doctorResponse.triggerKeywords.any { normalized.contains(it.lowercase()) }) {
            return doctorResponse
        }

        // Then other keywords
        return library.responses.firstOrNull { response ->
            response.triggerKeywords.any { keyword ->
                normalized.contains(keyword.lowercase())
            }
        }
    }
}
