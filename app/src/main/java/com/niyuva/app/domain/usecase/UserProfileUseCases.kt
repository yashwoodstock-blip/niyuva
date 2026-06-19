package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
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
    suspend operator fun invoke(pin: String) {
        val pinHash = BCrypt.withDefaults().hashToString(12, pin.toCharArray())
        repository.updatePinHash(pinHash)
    }
}

class VerifyPinUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(pin: String): Boolean {
        val profile = repository.getProfile()
        val pinHash = profile?.pinHash ?: return false
        return BCrypt.verifyer().verify(pin.toCharArray(), pinHash).verified
    }
}

class SetSecurityQuestionUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(question: String, answer: String) {
        val answerHash = BCrypt.withDefaults().hashToString(12, answer.trim().lowercase().toCharArray())
        repository.updateSecurityQuestion(question, answerHash)
    }
}

class ForgotPinVerifyAnswerUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(answer: String): Boolean {
        val profile = repository.getProfile()
        val answerHash = profile?.securityAnswerHash ?: return false
        return BCrypt.verifyer().verify(answer.trim().lowercase().toCharArray(), answerHash).verified
    }
}

class UpdateAiSettingsUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(enabled: Boolean, provider: AiProvider?) {
        repository.updateAiSettings(enabled, provider)
    }
}

class MarkOnboardingCompleteUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke() = repository.markOnboardingComplete()
}
