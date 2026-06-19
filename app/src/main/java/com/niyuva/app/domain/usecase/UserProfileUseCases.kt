package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import at.favre.lib.crypto.bcrypt.BCrypt
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile?> = repository.observeProfile()
    
    suspend fun getProfile(): UserProfile? = repository.getProfile()
}

class SaveUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile) = repository.saveProfile(profile)
}

class UpdateNameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(name: String) = repository.updateName(name)
}

class UpdateAgeUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(age: Int) = repository.updateAge(age)
}

class SetPinUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(pin: String) = withContext(Dispatchers.IO) {
        // SECURITY FIX: Wrap high-cost KDF hashing in Dispatchers.IO to protect Main thread
        val pinHash = BCrypt.withDefaults().hashToString(12, pin.toCharArray())
        repository.updatePinHash(pinHash)
    }
}

class VerifyPinUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(pin: String): Boolean = withContext(Dispatchers.IO) {
        val profile = repository.getProfile()
        val pinHash = profile?.pinHash ?: return@withContext false
        // SECURITY FIX: Wrap verification in Dispatchers.IO to protect Main thread
        BCrypt.verifyer().verify(pin.toCharArray(), pinHash).verified
    }
}

class SetSecurityQuestionUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(question: String, answer: String) = withContext(Dispatchers.IO) {
        // SECURITY FIX: Wrap security answer KDF hashing in Dispatchers.IO to protect Main thread
        val answerHash = BCrypt.withDefaults().hashToString(12, answer.trim().lowercase().toCharArray())
        repository.updateSecurityQuestion(question, answerHash)
    }
}

class ForgotPinVerifyAnswerUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(answer: String): Boolean = withContext(Dispatchers.IO) {
        val profile = repository.getProfile()
        val answerHash = profile?.securityAnswerHash ?: return@withContext false
        // SECURITY FIX: Wrap verification in Dispatchers.IO to protect Main thread
        BCrypt.verifyer().verify(answer.trim().lowercase().toCharArray(), answerHash).verified
    }
}


class MarkOnboardingCompleteUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke() = repository.markOnboardingComplete()
}
