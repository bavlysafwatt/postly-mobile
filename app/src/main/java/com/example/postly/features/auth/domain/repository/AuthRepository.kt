package com.example.postly.features.auth.domain.repository

import android.net.Uri
import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.auth.domain.model.AuthSession

interface AuthRepository {
    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        photoUri: Uri?
    ): Result<AuthSession, AppError>

    suspend fun login(email: String, password: String): Result<AuthSession, AppError>

    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<AuthSession, AppError>

    suspend fun forgotPassword(email: String): Result<Unit, AppError>

    suspend fun resetPassword(
        otp: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<AuthSession, AppError>
}