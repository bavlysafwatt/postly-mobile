package com.example.postly.features.settings.domain.usecase

import com.example.postly.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() = repository.logout()
}