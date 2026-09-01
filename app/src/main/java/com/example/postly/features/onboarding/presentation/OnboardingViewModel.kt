package com.example.postly.features.onboarding.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.features.onboarding.domain.usecases.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
        }
    }
}