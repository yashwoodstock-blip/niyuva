package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.niyuva.app.data.local.entity.CycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: CycleEntity): Long

    @Update
    suspend fun updateCycle(cycle: CycleEntity)

    @Delete
    suspend fun deleteCycle(cycle: CycleEntity)

    @Query("SELECT * FROM cycles ORDER BY start_date DESC")
    fun getAllCycles(): Flow<List<CycleEntity>>

    @Query("SELECT * FROM cycles ORDER BY start_date DESC LIMIT 1")
    suspend fun getLatestCycle(): CycleEntity?

    @Query("SELECT * FROM cycles ORDER BY start_date DESC LIMIT :count")
    suspend fun getRecentCycles(count: Int): List<CycleEntity>

    @Query("SELECT * FROM cycles WHERE id = :id")
    suspend fun getCycleById(id: Long): CycleEntity?

    @Query("DELETE FROM cycles")
    suspend fun deleteAllCycles()

    @Query("SELECT COUNT(*) FROM cycles")
    suspend fun getCycleCount(): Int
}
