package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.DailyLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLog(log: DailyLogEntity)

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    suspend fun getLogForDate(date: String): DailyLogEntity?

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getLogsInRange(startDate: String, endDate: String): List<DailyLogEntity>

    @Query("SELECT * FROM daily_logs ORDER BY date DESC LIMIT :count")
    suspend fun getRecentLogs(count: Int): List<DailyLogEntity>

    @Query("DELETE FROM daily_logs")
    suspend fun deleteAllLogs()

    @Query("SELECT COUNT(*) FROM daily_logs")
    suspend fun getLogCount(): Int
}
