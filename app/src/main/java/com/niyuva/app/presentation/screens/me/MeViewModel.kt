package com.niyuva.app.presentation.screens.me

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.data.local.entity.NotificationConfigEntity
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import com.niyuva.app.data.remote.AiRepository
import com.niyuva.app.domain.model.AiProvider
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.usecase.*
import java.time.LocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class TestConnectionStatus {
    LOADING, SUCCESS, FAILURE
}

@HiltViewModel
class MeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
    private val updateAgeUseCase: UpdateAgeUseCase,
    private val setPinUseCase: SetPinUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
    private val setSecurityQuestionUseCase: SetSecurityQuestionUseCase,
    private val forgotPinVerifyAnswerUseCase: ForgotPinVerifyAnswerUseCase,
    private val notificationConfigDao: NotificationConfigDao,
    private val aiRepository: AiRepository,
    private val userProfileRepository: UserProfileRepository,
    private val preferences: NiyuvaPreferences,
    private val exportBackupUseCase: ExportBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase,
    private val cycleRepository: CycleRepository,
    private val scheduleAllNotificationsUseCase: ScheduleAllNotificationsUseCase
) : ViewModel() {

    val profile: StateFlow<UserProfile?> = getUserProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isPinSet: StateFlow<Boolean> = getUserProfileUseCase()
        .map { it?.pinHash != null && it.pinHash.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isSecurityQuestionSet: StateFlow<Boolean> = getUserProfileUseCase()
        .map { it?.securityQuestion != null && it.securityQuestion.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val allNotificationConfigs: StateFlow<List<NotificationConfigEntity>> = notificationConfigDao.getAllConfigs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val showPinSheet = MutableStateFlow(false)
    val showSecuritySheet = MutableStateFlow(false)
    val showAiCard = MutableStateFlow(false)

    private val _testConnectionStatus = MutableStateFlow<TestConnectionStatus?>(null)
    val testConnectionStatus: StateFlow<TestConnectionStatus?> = _testConnectionStatus.asStateFlow()

    private val _connectionErrorMessage = MutableStateFlow<String?>(null)
    val connectionErrorMessage: StateFlow<String?> = _connectionErrorMessage.asStateFlow()

    fun updateName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNameUseCase(name.trim())
        }
    }

    fun updateAge(age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAgeUseCase(age)
        }
    }

    fun setShowPinSheet(show: Boolean) {
        showPinSheet.value = show
    }

    fun setShowSecuritySheet(show: Boolean) {
        showSecuritySheet.value = show
    }

    suspend fun verifyPin(pin: String): Boolean = withContext(Dispatchers.IO) {
        verifyPinUseCase(pin)
    }

    fun setPin(pin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setPinUseCase(pin)
        }
    }

    fun removePin() {
        viewModelScope.launch(Dispatchers.IO) {
            userProfileRepository.updatePinHash(null)
        }
    }

    suspend fun verifySecurityAnswer(answer: String): Boolean = withContext(Dispatchers.IO) {
        forgotPinVerifyAnswerUseCase(answer)
    }

    fun setSecurityQuestion(question: String, answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setSecurityQuestionUseCase(question, answer)
        }
    }

    fun updateNotificationEnabled(type: String, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationConfigDao.setEnabled(type, if (enabled) 1 else 0)
        }
    }

    fun toggleMasterMute(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationConfigDao.setEnabled("master_mute", if (enabled) 1 else 0)
            if (enabled) {
                // Muting: Save current state of other configs before turning them OFF
                val periodConfig = notificationConfigDao.getByType("period_prep")?.enabled ?: true
                val tipConfig = notificationConfigDao.getByType("daily_tip")?.enabled ?: true
                val ovulationConfig = notificationConfigDao.getByType("ovulation")?.enabled ?: true
                
                preferences.putBoolean("prev_period_prep", periodConfig)
                preferences.putBoolean("prev_daily_tip", tipConfig)
                preferences.putBoolean("prev_ovulation", ovulationConfig)
                
                notificationConfigDao.setEnabled("period_prep", 0)
                notificationConfigDao.setEnabled("daily_tip", 0)
                notificationConfigDao.setEnabled("ovulation", 0)
            } else {
                // Unmuting: Restore state from preferences
                val periodConfig = preferences.getBoolean("prev_period_prep", true)
                val tipConfig = preferences.getBoolean("prev_daily_tip", true)
                val ovulationConfig = preferences.getBoolean("prev_ovulation", true)
                
                notificationConfigDao.setEnabled("period_prep", if (periodConfig) 1 else 0)
                notificationConfigDao.setEnabled("daily_tip", if (tipConfig) 1 else 0)
                notificationConfigDao.setEnabled("ovulation", if (ovulationConfig) 1 else 0)
            }
        }
    }

    fun updateNotificationDaysBefore(type: String, days: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationConfigDao.setDaysBefore(type, days)
        }
    }

    fun updateNotificationTimeOfDay(type: String, time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationConfigDao.setTimeOfDay(type, time)
        }
    }

    @Suppress("SecretInLogs")
    fun saveApiKey(key: String, provider: AiProvider) {
        viewModelScope.launch(Dispatchers.IO) {
            if (key.isNotBlank()) {
                preferences.putString(NiyuvaPreferences.Keys.KEY_AI_API_KEY, key.trim())
                userProfileRepository.updateAiSettings(enabled = true, provider = provider)
            }
        }
    }

    fun disableAi() {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.remove(NiyuvaPreferences.Keys.KEY_AI_API_KEY)
            userProfileRepository.updateAiSettings(enabled = false, provider = null)
            _testConnectionStatus.value = null
            _connectionErrorMessage.value = null
        }
    }

    fun testConnection(key: String, provider: AiProvider) {
        viewModelScope.launch(Dispatchers.IO) {
            _testConnectionStatus.value = TestConnectionStatus.LOADING
            _connectionErrorMessage.value = null
            val error = aiRepository.testConnection(key.trim(), provider)
            if (error == null) {
                _testConnectionStatus.value = TestConnectionStatus.SUCCESS
            } else {
                _testConnectionStatus.value = TestConnectionStatus.FAILURE
                _connectionErrorMessage.value = error
            }
        }
    }

    fun clearConnectionTestStatus() {
        _testConnectionStatus.value = null
        _connectionErrorMessage.value = null
    }

    fun exportBackup(outputUri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = exportBackupUseCase(outputUri)
            withContext(Dispatchers.Main) {
                if (success) onSuccess() else onError("Backup export failed 💛")
            }
        }
    }

    fun restoreBackup(inputUri: Uri, onResult: (RestoreResult) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = restoreBackupUseCase(inputUri)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    fun confirmRestoreBackup(inputUri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = restoreBackupUseCase.confirmRestore(inputUri)
            withContext(Dispatchers.Main) {
                if (success) onSuccess() else onError("Backup restore failed 💛")
            }
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

    fun updateAverageCycleLength(length: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = userProfileRepository.getProfile() ?: return@launch
            userProfileRepository.saveProfile(currentProfile.copy(averageCycleLength = length))
            
            val latestCycle = cycleRepository.getLatestCycle()
            if (latestCycle != null) {
                cycleRepository.updateCycle(latestCycle.copy(cycleLength = length))
            }
            
            runCatching { scheduleAllNotificationsUseCase() }
        }
    }

    fun updateAveragePeriodLength(length: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = userProfileRepository.getProfile() ?: return@launch
            userProfileRepository.saveProfile(currentProfile.copy(averagePeriodLength = length))
            
            val latestCycle = cycleRepository.getLatestCycle()
            if (latestCycle != null) {
                cycleRepository.updateCycle(latestCycle.copy(periodLength = length))
            }
            
            runCatching { scheduleAllNotificationsUseCase() }
        }
    }

    fun updateLastPeriodStartDate(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = userProfileRepository.getProfile() ?: return@launch
            userProfileRepository.saveProfile(currentProfile.copy(lastPeriodStartDate = date))
            
            val latestCycle = cycleRepository.getLatestCycle()
            if (latestCycle != null) {
                cycleRepository.updateCycle(latestCycle.copy(startDate = date))
            } else {
                val newCycle = com.niyuva.app.domain.model.Cycle(
                    startDate = date,
                    cycleLength = currentProfile.averageCycleLength ?: 28,
                    periodLength = currentProfile.averagePeriodLength ?: 5
                )
                cycleRepository.saveCycle(newCycle)
            }
            
            runCatching { scheduleAllNotificationsUseCase() }
        }
    }
}
