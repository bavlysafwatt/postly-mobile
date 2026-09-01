package com.example.postly.features.onboarding.domain.usecases

import com.example.postly.core.data.local.OnboardingPreference
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val onboardingPreference: OnboardingPreference
) {
    suspend operator fun invoke() {
        onboardingPreference.setCompleted()
    }
}