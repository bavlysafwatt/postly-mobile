package com.example.postly.features.settings.domain.usecase

import com.example.postly.ui.theme.ThemeMode
import com.example.postly.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(mode: ThemeMode) = repository.setThemeMode(mode)
}