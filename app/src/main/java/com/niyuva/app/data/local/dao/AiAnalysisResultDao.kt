package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.AiAnalysisResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiAnalysisResultDao {
    @Query("SELECT * FROM ai_analysis_results ORDER BY id DESC")
    fun getAllResultsFlow(): Flow<List<AiAnalysisResultEntity>>

    @Query("SELECT * FROM ai_analysis_results ORDER BY id DESC")
    suspend fun getAllResults(): List<AiAnalysisResultEntity>

    @Query("SELECT * FROM ai_analysis_results WHERE analysis_type = :type AND chunk_sequence = :chunkSequence ORDER BY id DESC LIMIT 1")
    suspend fun getLatestResultForChunk(type: String, chunkSequence: Int): AiAnalysisResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateResult(result: AiAnalysisResultEntity)

    @Query("DELETE FROM ai_analysis_results")
    suspend fun deleteAllResults()
}
