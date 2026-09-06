package com.example.postly.features.settings.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<Unit, AppError> =
        repository.changePassword(currentPassword, newPassword, passwordConfirm)
}