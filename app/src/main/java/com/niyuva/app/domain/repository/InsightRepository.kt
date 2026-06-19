package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.Insight
import kotlinx.coroutines.flow.Flow

interface InsightRepository {
    fun getAllInsights(): Flow<List<Insight>>
    suspend fun saveInsight(insight: Insight)
    suspend fun getLatestInsightByType(type: String): Insight?
}
