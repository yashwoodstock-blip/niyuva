package com.niyuva.app.domain.model

import java.time.LocalDate

data class Insight(
    val id: Long = 0,
    val date: LocalDate,
    val insightType: String,
    val content: String,
    val source: String
)
