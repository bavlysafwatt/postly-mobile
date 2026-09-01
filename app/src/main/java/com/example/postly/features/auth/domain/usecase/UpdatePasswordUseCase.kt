package com.example.postly.features.auth.domain.usecase

import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val currentUserCache: CurrentUserCache
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<Unit, AppError> {
        val result = authRepository.updatePassword(currentPassword, newPassword, passwordConfirm)
        if (result is Result.Success) {
            tokenManager.saveToken(result.data.token)
            currentUserCache.save(result.data.user)
        }
        return result.map { }
    }
}