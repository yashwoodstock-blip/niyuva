package com.niyuva.app.domain.model

import java.time.LocalDate

data class Cycle(
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val cycleLength: Int? = null,
    val periodLength: Int? = null,
    val notes: String? = null
)
