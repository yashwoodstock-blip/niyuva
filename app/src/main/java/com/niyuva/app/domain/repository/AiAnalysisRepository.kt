package com.niyuva.app.domain.repository

import com.niyuva.app.data.local.entity.AiAnalysisResultEntity
import kotlinx.coroutines.flow.Flow

interface AiAnalysisRepository {
    fun getAllResultsFlow(): Flow<List<AiAnalysisResultEntity>>
    suspend fun getAllResults(): List<AiAnalysisResultEntity>
    suspend fun getLatestResultForChunk(type: String, chunkSequence: Int): AiAnalysisResultEntity?
    suspend fun insertOrUpdateResult(result: AiAnalysisResultEntity)
    suspend fun deleteAllResults()
}
