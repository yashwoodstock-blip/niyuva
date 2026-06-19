package com.niyuva.app.data.repository

import com.niyuva.app.data.local.dao.UserProfileDao
import com.niyuva.app.data.local.mapper.toDomain
import com.niyuva.app.data.local.mapper.toEntity
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserProfileRepository {

    override suspend fun getProfile(): UserProfile? = withContext(ioDispatcher) {
        userProfileDao.getProfile()?.toDomain()
    }

    override fun observeProfile(): Flow<UserProfile?> {
        return userProfileDao.observeProfile().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveProfile(profile: UserProfile) = withContext(ioDispatcher) {
        userProfileDao.insertOrUpdateProfile(profile.toEntity())
    }

    override suspend fun updateName(name: String) = withContext(ioDispatcher) {
        userProfileDao.updateName(name)
    }

    override suspend fun updateAge(age: Int) = withContext(ioDispatcher) {
        userProfileDao.updateAge(age)
    }

    override suspend fun updatePinHash(pinHash: String?) = withContext(ioDispatcher) {
        userProfileDao.updatePinHash(pinHash)
    }

    override suspend fun updateSecurityQuestion(question: String, answerHash: String) = withContext(ioDispatcher) {
        userProfileDao.updateSecurityQuestion(question, answerHash)
    }

    override suspend fun updateAiSettings(enabled: Boolean, provider: AiProvider?) = withContext(ioDispatcher) {
        userProfileDao.updateAiSettings(if (enabled) 1 else 0, provider?.name?.lowercase())
    }

    override suspend fun markOnboardingComplete() = withContext(ioDispatcher) {
        userProfileDao.markOnboardingComplete()
    }
}
