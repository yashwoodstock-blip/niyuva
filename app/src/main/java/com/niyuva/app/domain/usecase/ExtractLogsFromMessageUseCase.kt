package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.LogExtraction
import javax.inject.Inject

class ExtractLogsFromMessageUseCase @Inject constructor() {
    operator fun invoke(userMessage: String): LogExtraction? {
        val msg = userMessage.lowercase()
        return when {
            msg.containsAny("bahut flow", "heavy flow", "zyada flow")  -> LogExtraction("flow_level", "heavy")
            msg.containsAny("flow kam", "light flow", "thoda flow")    -> LogExtraction("flow_level", "light")
            msg.containsAny("spotting")                                 -> LogExtraction("flow_level", "spotting")
            msg.containsAny("bahut dard", "severe dard", "tez dard")  -> LogExtraction("pain_level", "severe")
            msg.containsAny("mild dard", "thoda dard")                 -> LogExtraction("pain_level", "mild")
            msg.containsAny("thaka", "thaki", "energy nahi", "tired") -> LogExtraction("energy_level", "low")
            msg.containsAny("bahut energy", "active hoon")             -> LogExtraction("energy_level", "high")
            msg.containsAny("mood nahi", "irritated", "sad", "stressed") -> LogExtraction("mood", "stressed")
            msg.containsAny("khush", "happy", "excited")               -> LogExtraction("mood", "happy")
            else -> null
        }
    }
    private fun String.containsAny(vararg keywords: String) = keywords.any { this.contains(it) }
}
