package com.example.postly.features.settings.domain.usecase

import com.example.postly.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePushNotificationsEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.observePushNotificationsEnabled()
}