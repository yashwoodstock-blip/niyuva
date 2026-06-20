package com.niyuva.app.domain.model

data class DidiResponse(
    val id: String,
    val triggerKeywords: List<String>,   // any of these keywords in user message triggers this response
    val response: String,                // the Hinglish response text
    val logExtracted: LogExtraction? = null   // if this response also logs something
)

data class LogExtraction(
    val field: String,   // "flow_level", "pain_level", "energy_level", "mood", "discharge_type", "sleep_quality"
    val value: String    // the value to set
)
