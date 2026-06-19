package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.StreakDao
import com.niyuva.app.data.local.entity.StreakEntity
import com.niyuva.app.domain.repository.StreakRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreakRepositoryImpl @Inject constructor(
    private val streakDao: StreakDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StreakRepository {

    override fun getStreakFlow(): Flow<StreakEntity?> {
        return streakDao.getStreakFlow()
    }

    override suspend fun getStreak(): StreakEntity? = withContext(ioDispatcher) {
        streakDao.getStreak()
    }

    override suspend fun updateStreak(streak: StreakEntity) = withContext(ioDispatcher) {
        streakDao.insertOrUpdateStreak(streak)
    }

    override suspend fun clearStreak() = withContext(ioDispatcher) {
        streakDao.clearStreak()
    }
}
