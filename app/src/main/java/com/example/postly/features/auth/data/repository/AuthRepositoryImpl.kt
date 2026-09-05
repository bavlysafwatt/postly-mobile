package com.example.postly.features.auth.data.repository

import android.content.Context
import android.net.Uri
import com.example.postly.core.data.dto.toDomain
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.core.network.safeApiCallUnit
import com.example.postly.features.auth.data.dto.ForgotPasswordRequest
import com.example.postly.features.auth.data.dto.LoginRequest
import com.example.postly.features.auth.data.dto.ResetPasswordRequest
import com.example.postly.features.auth.data.remote.AuthApi
import com.example.postly.features.auth.domain.model.AuthSession
import com.example.postly.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        photoUri: Uri?
    ): Result<AuthSession, AppError> {
        val photoPart = photoUri?.let { uriToMultipart(it, "photo") }

        val result = safeApiCall {
            api.register(
                name = name.toPlainRequestBody(),
                username = username.toPlainRequestBody(),
                email = email.toPlainRequestBody(),
                password = password.toPlainRequestBody(),
                confirmPassword = confirmPassword.toPlainRequestBody(),
                photo = photoPart
            )
        }
        return result.map { AuthSession(token = it.accessToken, user = it.user.toDomain()) }
    }

    override suspend fun login(email: String, password: String): Result<AuthSession, AppError> {
        val result = safeApiCall { api.login(LoginRequest(email, password)) }
        return result.map { AuthSession(token = it.accessToken, user = it.user.toDomain()) }
    }

    override suspend fun forgotPassword(email: String): Result<Unit, AppError> =
        safeApiCallUnit { api.forgotPassword(ForgotPasswordRequest(email)) }

    override suspend fun resetPassword(
        otp: String,
        newPassword: String,
        passwordConfirm: String
    ): Result<AuthSession, AppError> {
        val result = safeApiCall {
            api.resetPassword(ResetPasswordRequest(otp, newPassword, passwordConfirm))
        }
        return result.map { AuthSession(token = it.accessToken, user = it.user.toDomain()) }
    }

    private fun String.toPlainRequestBody() = toRequestBody("text/plain".toMediaTypeOrNull())

    private fun uriToMultipart(uri: Uri, partName: String): MultipartBody.Part? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = inputStream.use { it.readBytes() }
        val mimeType = context.contentResolver.getType(uri) ?: "image/*"
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }
}