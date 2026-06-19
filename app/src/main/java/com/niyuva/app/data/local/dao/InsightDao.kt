package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.InsightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: InsightEntity)

    @Query("SELECT * FROM insights ORDER BY date DESC")
    fun getAllInsights(): Flow<List<InsightEntity>>

    @Query("SELECT * FROM insights WHERE insight_type = :type ORDER BY date DESC LIMIT 1")
    suspend fun getLatestInsightByType(type: String): InsightEntity?

    @Query("DELETE FROM insights")
    suspend fun deleteAllInsights()
}
