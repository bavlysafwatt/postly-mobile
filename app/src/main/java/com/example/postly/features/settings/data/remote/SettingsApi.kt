package com.example.postly.features.settings.data.remote

import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.settings.data.dto.UpdatePasswordEnvelope
import com.example.postly.features.settings.data.dto.UpdatePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SettingsApi {
    @POST("api/v1/auth/update-password")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<BaseResponse<UpdatePasswordEnvelope>>
}