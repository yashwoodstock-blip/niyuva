package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.InsightDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.Insight
import com.niyuva.app.domain.repository.InsightRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsightRepositoryImpl @Inject constructor(
    private val insightDao: InsightDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InsightRepository {

    override fun getAllInsights(): Flow<List<Insight>> {
        return insightDao.getAllInsights().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveInsight(insight: Insight) = withContext(ioDispatcher) {
        insightDao.insertInsight(insight.toEntity())
    }

    override suspend fun getLatestInsightByType(type: String): Insight? = withContext(ioDispatcher) {
        insightDao.getLatestInsightByType(type)?.toDomain()
    }
}
