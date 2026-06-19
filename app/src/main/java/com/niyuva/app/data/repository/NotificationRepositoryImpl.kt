package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.NotificationConfig
import com.niyuva.app.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationConfigDao: NotificationConfigDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NotificationRepository {

    override suspend fun saveConfig(config: NotificationConfig) = withContext(ioDispatcher) {
        notificationConfigDao.insertOrUpdate(config.toEntity())
    }

    override fun getAllConfigs(): Flow<List<NotificationConfig>> {
        return notificationConfigDao.getAllConfigs().map { list ->
            list.map { it.toDomain() }
        }
    }
}
