package com.example.postly.features.settings.domain.usecase

import com.example.postly.features.settings.domain.repository.SettingsRepository
import com.example.postly.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveThemeModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeMode> = repository.observeThemeMode()
}