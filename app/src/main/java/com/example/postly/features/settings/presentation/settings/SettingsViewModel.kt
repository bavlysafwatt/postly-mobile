package com.example.postly.features.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.settings.domain.usecase.LogoutUseCase
import com.example.postly.features.settings.domain.usecase.ObservePushNotificationsEnabledUseCase
import com.example.postly.features.settings.domain.usecase.ObserveThemeModeUseCase
import com.example.postly.features.settings.domain.usecase.SetPushNotificationsEnabledUseCase
import com.example.postly.features.settings.domain.usecase.SetThemeModeUseCase
import com.example.postly.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val logoutUseCase: LogoutUseCase,
    observePushNotificationsEnabledUseCase: ObservePushNotificationsEnabledUseCase,
    private val setPushNotificationsEnabledUseCase: SetPushNotificationsEnabledUseCase
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = observeThemeModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val pushEnabled: StateFlow<Boolean> = observePushNotificationsEnabledUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { setThemeModeUseCase(mode) }
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val result = setPushNotificationsEnabledUseCase(enabled)
            if (result is Result.Failure) {
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _isLoggedOut.value = true
        }
    }
}