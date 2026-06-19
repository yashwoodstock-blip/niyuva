package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.Cycle
import kotlinx.coroutines.flow.Flow

interface CycleRepository {
    fun getAllCycles(): Flow<List<Cycle>>
    suspend fun getLatestCycle(): Cycle?
    suspend fun getRecentCycles(count: Int): List<Cycle>
    suspend fun saveCycle(cycle: Cycle): Long
    suspend fun updateCycle(cycle: Cycle)
    suspend fun getCycleCount(): Int
}
