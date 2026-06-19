package com.niyuva.app.domain.repository

import com.niyuva.app.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

interface StreakRepository {
    fun getStreakFlow(): Flow<StreakEntity?>
    suspend fun getStreak(): StreakEntity?
    suspend fun updateStreak(streak: StreakEntity)
    suspend fun clearStreak()
}
