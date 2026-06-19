package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.AiAnalysisResultDao
import com.niyuva.app.data.local.entity.AiAnalysisResultEntity
import com.niyuva.app.domain.repository.AiAnalysisRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiAnalysisRepositoryImpl @Inject constructor(
    private val aiAnalysisResultDao: AiAnalysisResultDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AiAnalysisRepository {

    override fun getAllResultsFlow(): Flow<List<AiAnalysisResultEntity>> {
        return aiAnalysisResultDao.getAllResultsFlow()
    }

    override suspend fun getAllResults(): List<AiAnalysisResultEntity> = withContext(ioDispatcher) {
        aiAnalysisResultDao.getAllResults()
    }

    override suspend fun getLatestResultForChunk(type: String, chunkSequence: Int): AiAnalysisResultEntity? = withContext(ioDispatcher) {
        aiAnalysisResultDao.getLatestResultForChunk(type, chunkSequence)
    }

    override suspend fun insertOrUpdateResult(result: AiAnalysisResultEntity) = withContext(ioDispatcher) {
        aiAnalysisResultDao.insertOrUpdateResult(result)
    }

    override suspend fun deleteAllResults() = withContext(ioDispatcher) {
        aiAnalysisResultDao.deleteAllResults()
    }
}
