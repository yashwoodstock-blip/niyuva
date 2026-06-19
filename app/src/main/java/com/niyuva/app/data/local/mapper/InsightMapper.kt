package com.niyuva.app.data.local.mapper

import com.niyuva.app.data.local.entity.InsightEntity
import com.niyuva.app.domain.model.Insight
import java.time.LocalDate
import java.time.LocalDateTime

fun InsightEntity.toDomain(): Insight {
    return Insight(
        id = id?.toLong() ?: 0L,
        date = LocalDate.parse(date),
        insightType = insightType,
        content = content,
        source = source
    )
}

fun Insight.toEntity(): InsightEntity {
    return InsightEntity(
        id = if (id == 0L) null else id.toInt(),
        date = date.toString(),
        insightType = insightType,
        content = content,
        source = source,
        createdAt = LocalDateTime.now()
    )
}
