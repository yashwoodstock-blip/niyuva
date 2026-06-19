package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.CycleDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.repository.CycleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CycleRepositoryImpl @Inject constructor(
    private val cycleDao: CycleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CycleRepository {

    override fun getAllCycles(): Flow<List<Cycle>> {
        return cycleDao.getAllCycles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLatestCycle(): Cycle? = withContext(ioDispatcher) {
        cycleDao.getLatestCycle()?.toDomain()
    }

    override suspend fun getRecentCycles(count: Int): List<Cycle> = withContext(ioDispatcher) {
        cycleDao.getRecentCycles(count).map { it.toDomain() }
    }

    override suspend fun saveCycle(cycle: Cycle): Long = withContext(ioDispatcher) {
        cycleDao.insertCycle(cycle.toEntity())
    }

    override suspend fun updateCycle(cycle: Cycle) = withContext(ioDispatcher) {
        cycleDao.updateCycle(cycle.toEntity())
    }

    override suspend fun getCycleCount(): Int = withContext(ioDispatcher) {
        cycleDao.getCycleCount()
    }
}
