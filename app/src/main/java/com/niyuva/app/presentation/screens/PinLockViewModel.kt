package com.niyuva.app.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.usecase.ForgotPinVerifyAnswerUseCase
import com.niyuva.app.domain.usecase.GetUserProfileUseCase
import com.niyuva.app.domain.usecase.SetPinUseCase
import com.niyuva.app.domain.usecase.VerifyPinUseCase
import com.niyuva.app.domain.usecase.DeleteAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface LockoutState {
    object None : LockoutState
    data class Locked(val timeRemainingMs: Long) : LockoutState
}

@HiltViewModel
class PinLockViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
    private val forgotPinVerifyAnswerUseCase: ForgotPinVerifyAnswerUseCase,
    private val setPinUseCase: SetPinUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase,
    private val preferences: NiyuvaPreferences
) : ViewModel() {

    val profile: StateFlow<UserProfile?> = getUserProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _wrongAttempts = MutableStateFlow(0)
    val wrongAttempts: StateFlow<Int> = _wrongAttempts.asStateFlow()

    private val _lockoutState = MutableStateFlow<LockoutState>(LockoutState.None)
    val lockoutState: StateFlow<LockoutState> = _lockoutState.asStateFlow()

    init {
        // REFACTOR: Initialize wrong attempts count from preferences
        _wrongAttempts.value = getWrongAttemptsFromPrefs()
        startLockoutTicker()
    }

    private fun startLockoutTicker() {
        viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val exp = getLockoutExpiration()
                if (exp > now) {
                    _lockoutState.value = LockoutState.Locked(exp - now)
                } else {
                    _lockoutState.value = LockoutState.None
                }
                delay(1000)
            }
        }
    }

    private fun getLockoutExpiration(): Long {
        return preferences.getString("lockout_expiration_timestamp")?.toLongOrNull() ?: 0L
    }

    private fun setLockoutExpiration(timestamp: Long) {
        preferences.putString("lockout_expiration_timestamp", timestamp.toString())
    }

    private fun getWrongAttemptsFromPrefs(): Int {
        return preferences.getString("consecutive_pin_failures")?.toIntOrNull() ?: 0
    }

    private fun setWrongAttemptsInPrefs(count: Int) {
        preferences.putString("consecutive_pin_failures", count.toString())
        _wrongAttempts.value = count
    }

    fun incrementWrongAttempts() {
        val nextAttempts = getWrongAttemptsFromPrefs() + 1
        setWrongAttemptsInPrefs(nextAttempts)
        
        // SECURITY FIX: Apply exponential lockout durations after 5, 10, 20 failures
        val duration = when {
            nextAttempts >= 20 -> 15 * 60 * 1000L // 15 mins
            nextAttempts >= 10 -> 5 * 60 * 1000L  // 5 mins
            nextAttempts >= 5 -> 30 * 1000L       // 30 seconds
            else -> 0L
        }
        if (duration > 0) {
            setLockoutExpiration(System.currentTimeMillis() + duration)
        }
    }

    fun resetWrongAttempts() {
        setWrongAttemptsInPrefs(0)
        setLockoutExpiration(0L)
    }

    suspend fun verifyPin(pin: String): Boolean = withContext(Dispatchers.IO) {
        // SECURITY FIX: Reject pin verification if lockout is active
        val now = System.currentTimeMillis()
        if (getLockoutExpiration() > now) {
            return@withContext false
        }
        val success = verifyPinUseCase(pin)
        if (success) {
            resetWrongAttempts()
        } else {
            incrementWrongAttempts()
        }
        success
    }

    suspend fun verifySecurityAnswer(answer: String): Boolean = withContext(Dispatchers.IO) {
        forgotPinVerifyAnswerUseCase(answer)
    }

    fun setPin(pin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setPinUseCase(pin)
            resetWrongAttempts()
        }
    }

    fun deleteAllData(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllDataUseCase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}
