package com.example.postly.features.auth.domain.usecase

import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.PushNotificationPreference
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.core.push.PushNotificationManager
import com.example.postly.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val currentUserCache: CurrentUserCache,
    private val pushNotificationPreference: PushNotificationPreference,
    private val pushNotificationManager: PushNotificationManager
) {
    suspend operator fun invoke(
        otp: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<Unit, AppError> {
        val result = authRepository.resetPassword(otp, newPassword, passwordConfirm)
        if (result is Result.Success) {
            tokenManager.saveToken(result.data.token)
            currentUserCache.save(result.data.user)
            resubscribeToPushIfEnabled(result.data.user.id)
        }
        return result.map { }
    }

    private suspend fun resubscribeToPushIfEnabled(userId: String) {
        if (pushNotificationPreference.isEnabled()) {
            runCatching { pushNotificationManager.subscribeForUser(userId) }
        }
    }
}