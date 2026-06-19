package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.NotificationConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(config: NotificationConfigEntity)

    @Query("SELECT * FROM notifications_config WHERE type = :type")
    suspend fun getByType(type: String): NotificationConfigEntity?

    @Query("SELECT * FROM notifications_config")
    fun getAllConfigs(): Flow<List<NotificationConfigEntity>>

    @Query("UPDATE notifications_config SET enabled = :enabled WHERE type = :type")
    suspend fun setEnabled(type: String, enabled: Int)

    @Query("UPDATE notifications_config SET days_before = :daysBefore WHERE type = :type")
    suspend fun setDaysBefore(type: String, daysBefore: Int)

    @Query("UPDATE notifications_config SET time_of_day = :timeOfDay WHERE type = :type")
    suspend fun setTimeOfDay(type: String, timeOfDay: String)

    @Query("DELETE FROM notifications_config")
    suspend fun deleteAll()
}
