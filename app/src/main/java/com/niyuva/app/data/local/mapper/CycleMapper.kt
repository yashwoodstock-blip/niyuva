package com.niyuva.app.data.local.mapper

import com.niyuva.app.data.local.entity.CycleEntity
import com.niyuva.app.domain.model.Cycle
import java.time.LocalDateTime

fun CycleEntity.toDomain(): Cycle {
    return Cycle(
        id = id?.toLong() ?: 0L,
        startDate = startDate,
        endDate = endDate,
        cycleLength = cycleLength,
        periodLength = periodLength,
        notes = notes
    )
}

fun Cycle.toEntity(createdAt: LocalDateTime = LocalDateTime.now()): CycleEntity {
    return CycleEntity(
        id = if (id == 0L) null else id.toInt(),
        startDate = startDate,
        endDate = endDate,
        cycleLength = cycleLength,
        periodLength = periodLength,
        notes = notes,
        createdAt = createdAt
    )
}
