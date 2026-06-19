package com.niyuva.app.data.local.mapper

import com.niyuva.app.data.local.entity.UserProfileEntity
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.model.UserProfile
import java.time.LocalDateTime

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        age = age,
        averageCycleLength = averageCycleLength,
        averagePeriodLength = averagePeriodLength,
        lastPeriodStartDate = lastPeriodStartDate,
        pinHash = pinHash,
        securityQuestion = securityQuestion,
        securityAnswerHash = securityAnswerHash,
        aiEnabled = aiEnabled,
        aiProvider = AiProvider.fromString(aiProvider),
        onboardingComplete = onboardingComplete
    )
}

fun UserProfile.toEntity(createdAt: LocalDateTime = LocalDateTime.now()): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        name = name,
        age = age,
        averageCycleLength = averageCycleLength,
        averagePeriodLength = averagePeriodLength,
        lastPeriodStartDate = lastPeriodStartDate,
        pinHash = pinHash,
        securityQuestion = securityQuestion,
        securityAnswerHash = securityAnswerHash,
        aiEnabled = aiEnabled,
        aiProvider = aiProvider?.name?.lowercase(),
        onboardingComplete = onboardingComplete,
        createdAt = createdAt
    )
}
