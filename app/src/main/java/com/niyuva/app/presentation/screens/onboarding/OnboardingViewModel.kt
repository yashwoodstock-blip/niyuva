package com.niyuva.app.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class OnboardingState(
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val userName: String = "",
    val selectedAge: Int? = null,
    val lastPeriodDate: LocalDate? = null,
    val cycleLengthDays: Int? = null,
    val periodLengthDays: Int? = null,
    val pinSet: Boolean = false,
    val securityQuestionSet: Boolean = false,
    val periodReminderEnabled: Boolean = true,
    val dailyTipEnabled: Boolean = true,
    val ovulationReminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOnboardingComplete: Boolean = false
)

enum class OnboardingStep {
    WELCOME, PRIVACY, NAME, AGE_SELECT, LAST_PERIOD, CYCLE_BASICS,
    PIN_SETUP, SECURITY_QUESTION, NOTIFICATIONS, CELEBRATION
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val saveCycleUseCase: SaveCycleUseCase,
    private val setPinUseCase: SetPinUseCase,
    private val setSecurityQuestionUseCase: SetSecurityQuestionUseCase,
    private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase,
    private val insertDefaultNotificationsUseCase: InsertDefaultNotificationsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun onWelcomeComplete() {
        _state.update { it.copy(currentStep = OnboardingStep.PRIVACY) }
    }

    fun onPrivacyComplete() {
        _state.update { it.copy(currentStep = OnboardingStep.NAME) }
    }

    fun onNameEntered(name: String) {
        _state.update { it.copy(userName = name, currentStep = OnboardingStep.AGE_SELECT) }
    }

    fun onAgeSelected(yearOfBirth: Int) {
        val currentYear = LocalDate.now().year
        val calculatedAge = currentYear - yearOfBirth
        _state.update {
            it.copy(
                selectedAge = calculatedAge,
                currentStep = OnboardingStep.LAST_PERIOD
            )
        }
    }

    fun onLastPeriodDateSelected(date: LocalDate) {
        _state.update { it.copy(lastPeriodDate = date, currentStep = OnboardingStep.CYCLE_BASICS) }
    }

    fun onCycleBasicsEntered(cycleLength: Int?, periodLength: Int?) {
        _state.update {
            it.copy(
                cycleLengthDays = cycleLength,
                periodLengthDays = periodLength,
                currentStep = OnboardingStep.PIN_SETUP
            )
        }
    }

    fun onPinSet(pin: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                setPinUseCase(pin)
                _state.update {
                    it.copy(
                        pinSet = true,
                        isLoading = false,
                        currentStep = OnboardingStep.SECURITY_QUESTION
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Kuch hua — phir try karo 💛"
                    )
                }
            }
        }
    }

    fun onPinSkipped() {
        _state.update {
            it.copy(
                pinSet = false,
                currentStep = OnboardingStep.NOTIFICATIONS
            )
        }
    }

    fun onSecurityQuestionSet(question: String, answer: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                setSecurityQuestionUseCase(question, answer)
                _state.update {
                    it.copy(
                        securityQuestionSet = true,
                        isLoading = false,
                        currentStep = OnboardingStep.NOTIFICATIONS
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Kuch hua — phir try karo 💛"
                    )
                }
            }
        }
    }

    fun onNotificationsConfigured(period: Boolean, daily: Boolean, ovulation: Boolean) {
        _state.update {
            it.copy(
                periodReminderEnabled = period,
                dailyTipEnabled = daily,
                ovulationReminderEnabled = ovulation,
                currentStep = OnboardingStep.CELEBRATION
            )
        }
    }

    fun onOnboardingComplete() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }

                // 1. Get existing profile or create a default one
                val currentProfile = getUserProfileUseCase.getProfile() ?: UserProfile()

                // 2. Save UserProfile with collected name, averageCycleLength, averagePeriodLength, lastPeriodStartDate
                val updatedProfile = currentProfile.copy(
                    name = _state.value.userName,
                    age = _state.value.selectedAge,
                    averageCycleLength = _state.value.cycleLengthDays,
                    averagePeriodLength = _state.value.periodLengthDays,
                    lastPeriodStartDate = _state.value.lastPeriodDate,
                    onboardingComplete = false
                )
                saveUserProfileUseCase(updatedProfile)

                // 3. Save initial cycle if lastPeriodDate != null
                _state.value.lastPeriodDate?.let { startDate ->
                    val initialCycle = Cycle(
                        startDate = startDate,
                        cycleLength = _state.value.cycleLengthDays,
                        periodLength = _state.value.periodLengthDays
                    )
                    saveCycleUseCase(initialCycle)
                }

                // 4. Check system notification permission
                val isPermissionGranted = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    androidx.core.content.ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }

                // 5. Insert default notifications config seed using user's preferences
                insertDefaultNotificationsUseCase(
                    periodReminderEnabled = isPermissionGranted && _state.value.periodReminderEnabled,
                    dailyTipEnabled = isPermissionGranted && _state.value.dailyTipEnabled,
                    ovulationReminderEnabled = isPermissionGranted && _state.value.ovulationReminderEnabled
                )

                // 6. Mark onboarding complete in DB
                markOnboardingCompleteUseCase()

                // 6.5. Schedule all notifications
                scheduleAllNotificationsUseCase()

                // 7. Update state
                _state.update { it.copy(isLoading = false, isOnboardingComplete = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Kuch hua — phir try karo 💛"
                    )
                }
            }
        }
    }

    fun onBackClicked() {
        val prevStep = when (_state.value.currentStep) {
            OnboardingStep.WELCOME -> return
            OnboardingStep.PRIVACY -> OnboardingStep.WELCOME
            OnboardingStep.NAME -> OnboardingStep.PRIVACY
            OnboardingStep.AGE_SELECT -> OnboardingStep.NAME
            OnboardingStep.LAST_PERIOD -> OnboardingStep.AGE_SELECT
            OnboardingStep.CYCLE_BASICS -> OnboardingStep.LAST_PERIOD
            OnboardingStep.PIN_SETUP -> OnboardingStep.CYCLE_BASICS
            OnboardingStep.SECURITY_QUESTION -> OnboardingStep.PIN_SETUP
            OnboardingStep.NOTIFICATIONS -> {
                if (_state.value.pinSet) {
                    OnboardingStep.SECURITY_QUESTION
                } else {
                    OnboardingStep.PIN_SETUP
                }
            }
            OnboardingStep.CELEBRATION -> OnboardingStep.NOTIFICATIONS
        }
        _state.update { it.copy(currentStep = prevStep) }
    }
}
