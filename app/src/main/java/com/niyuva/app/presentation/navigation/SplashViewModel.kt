package com.niyuva.app.presentation.navigation

import androidx.lifecycle.ViewModel
import com.niyuva.app.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    suspend fun checkStartupNavigation(): StartupDestination {
        val profile = userProfileRepository.getProfile()
        return if (profile == null || !profile.onboardingComplete) {
            StartupDestination.ONBOARDING
        } else if (!profile.pinHash.isNullOrEmpty()) {
            StartupDestination.PIN_LOCK
        } else {
            StartupDestination.MAIN
        }
    }
}

enum class StartupDestination {
    ONBOARDING, PIN_LOCK, MAIN
}
