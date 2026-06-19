package com.niyuva.app.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyuva.app.domain.model.UserProfile
import com.niyuva.app.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface UserProfileState {
    object Loading : UserProfileState
    data class Success(val profile: UserProfile?) : UserProfileState
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val profileState: StateFlow<UserProfileState> = getUserProfileUseCase()
        .map { UserProfileState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileState.Loading
        )
}
