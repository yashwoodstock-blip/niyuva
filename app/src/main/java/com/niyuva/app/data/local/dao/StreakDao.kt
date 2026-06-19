package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM streaks WHERE id = 1")
    fun getStreakFlow(): Flow<StreakEntity?>

    @Query("SELECT * FROM streaks WHERE id = 1")
    suspend fun getStreak(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStreak(streak: StreakEntity)

    @Query("DELETE FROM streaks")
    suspend fun clearStreak()
}
