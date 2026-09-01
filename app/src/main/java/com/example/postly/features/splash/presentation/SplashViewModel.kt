package com.example.postly.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.data.local.OnboardingPreference
import com.example.postly.core.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SplashDestination {
    Onboarding, Home, Login
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val onboardingPreference: OnboardingPreference
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val minDuration = async { delay(1000) }

            val hasCompletedOnboarding = onboardingPreference.hasCompletedOnboarding()
            val isLoggedIn = tokenManager.isTokenPresentAndNotExpired()

            minDuration.await()

            _destination.value = when {
                !hasCompletedOnboarding -> SplashDestination.Onboarding
                isLoggedIn -> SplashDestination.Home
                else -> SplashDestination.Login
            }
        }
    }
}