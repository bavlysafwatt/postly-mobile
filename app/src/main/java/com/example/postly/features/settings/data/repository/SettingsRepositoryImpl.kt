package com.example.postly.features.settings.data.repository

import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.PushNotificationPreference
import com.example.postly.core.data.local.ThemeModePreference
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.core.push.PushNotificationManager
import com.example.postly.features.settings.data.dto.UpdatePasswordRequest
import com.example.postly.features.settings.data.remote.SettingsApi
import com.example.postly.features.settings.domain.repository.SettingsRepository
import com.example.postly.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val api: SettingsApi,
    private val themeModePreference: ThemeModePreference,
    private val tokenManager: TokenManager,
    private val currentUserCache: CurrentUserCache,
    private val pushNotificationPreference: PushNotificationPreference,
    private val pushNotificationManager: PushNotificationManager
) : SettingsRepository {

    override fun observeThemeMode(): Flow<ThemeMode> = themeModePreference.observeThemeMode()

    override suspend fun setThemeMode(mode: ThemeMode) = themeModePreference.setThemeMode(mode)

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<Unit, AppError> {
        val result = safeApiCall {
            api.updatePassword(UpdatePasswordRequest(currentPassword, newPassword, passwordConfirm))
        }
        if (result is Result.Success) {
            tokenManager.saveToken(result.data.accessToken)
        }
        return result.map { }
    }

    override suspend fun logout() {
        val userId = currentUserCache.observeUser().first()?.id
        if (userId != null && pushNotificationPreference.isEnabled()) {
            runCatching { pushNotificationManager.unsubscribeForUser(userId) }
        }
        tokenManager.clearToken()
        currentUserCache.clear()
    }

    override fun observePushNotificationsEnabled(): Flow<Boolean> =
        pushNotificationPreference.observeEnabled()

    override suspend fun setPushNotificationsEnabled(enabled: Boolean): Result<Unit, AppError> {
        val userId = currentUserCache.observeUser().first()?.id
            ?: return Result.Failure(AppError.Unknown("No signed-in user"))

        return try {
            if (enabled) {
                pushNotificationManager.subscribeForUser(userId)
            } else {
                pushNotificationManager.unsubscribeForUser(userId)
            }
            pushNotificationPreference.setEnabled(enabled)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(AppError.Unknown(e.message))
        }
    }
}