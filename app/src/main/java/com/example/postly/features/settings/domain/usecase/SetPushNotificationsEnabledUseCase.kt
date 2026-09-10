package com.example.postly.features.settings.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetPushNotificationsEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit, AppError> =
        repository.setPushNotificationsEnabled(enabled)
}