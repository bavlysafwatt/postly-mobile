package com.example.postly.features.auth.data.remote

import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.auth.data.dto.AuthEnvelope
import com.example.postly.features.auth.data.dto.ForgotPasswordRequest
import com.example.postly.features.auth.data.dto.LoginRequest
import com.example.postly.features.auth.data.dto.ResetPasswordRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @Multipart
    @POST("api/v1/auth/register")
    suspend fun register(
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Response<BaseResponse<AuthEnvelope>>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<AuthEnvelope>>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<BaseResponse<Unit?>>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<AuthEnvelope>>
}