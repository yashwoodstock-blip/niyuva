package com.niyuva.app.domain.repository

import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile?
    fun observeProfile(): Flow<UserProfile?>
    suspend fun saveProfile(profile: UserProfile)
    suspend fun updateName(name: String)
    suspend fun updateAge(age: Int)
    suspend fun updatePinHash(pinHash: String?)
    suspend fun updateSecurityQuestion(question: String, answerHash: String)
    suspend fun updateAiSettings(enabled: Boolean, provider: AiProvider?)
    suspend fun markOnboardingComplete()
}
