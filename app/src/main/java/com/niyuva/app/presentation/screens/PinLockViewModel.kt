package com.niyuva.app.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.usecase.ForgotPinVerifyAnswerUseCase
import com.niyuva.app.domain.usecase.GetUserProfileUseCase
import com.niyuva.app.domain.usecase.SetPinUseCase
import com.niyuva.app.domain.usecase.VerifyPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

import com.niyuva.app.domain.usecase.DeleteAllDataUseCase

@HiltViewModel
class PinLockViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
    private val forgotPinVerifyAnswerUseCase: ForgotPinVerifyAnswerUseCase,
    private val setPinUseCase: SetPinUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val profile: StateFlow<UserProfile?> = getUserProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _wrongAttempts = MutableStateFlow(0)
    val wrongAttempts: StateFlow<Int> = _wrongAttempts.asStateFlow()

    fun incrementWrongAttempts() {
        _wrongAttempts.value += 1
    }

    fun resetWrongAttempts() {
        _wrongAttempts.value = 0
    }

    suspend fun verifyPin(pin: String): Boolean = withContext(Dispatchers.IO) {
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
