package com.example.postly.features.settings.domain.repository

import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<Unit, AppError>
    suspend fun logout()
    fun observePushNotificationsEnabled(): Flow<Boolean>
    suspend fun setPushNotificationsEnabled(enabled: Boolean): Result<Unit, AppError>
}