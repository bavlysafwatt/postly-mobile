package com.example.postly.features.auth.domain.usecase


import android.net.Uri
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val currentUserCache: CurrentUserCache
) {
    suspend operator fun invoke(
        name: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        photoUri: Uri?
    ): Result<Unit, AppError> {
        val result =
            authRepository.register(name, username, email, password, confirmPassword, photoUri)
        if (result is Result.Success) {
            tokenManager.saveToken(result.data.token)
            currentUserCache.save(result.data.user)
        }
        return result.map { }
    }
}