package com.example.postly.features.notifications.data.remote

import com.example.postly.core.data.dto.BaseResponse
import com.example.postly.features.notifications.data.dto.NotificationEnvelope
import com.example.postly.features.notifications.data.dto.NotificationsEnvelope
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BaseResponse<NotificationsEnvelope>>

    @PATCH("api/v1/notifications/{id}/read")
    suspend fun markAsRead(@Path("id") notificationId: String): Response<BaseResponse<NotificationEnvelope>>

    // No `data` field at all on success -- routed through safeApiCallUnit.
    @PATCH("api/v1/notifications/read-all")
    suspend fun markAllAsRead(): Response<BaseResponse<Unit?>>
}