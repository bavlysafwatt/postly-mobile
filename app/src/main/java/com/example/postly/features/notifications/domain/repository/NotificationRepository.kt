package com.example.postly.features.notifications.domain.repository

import androidx.paging.PagingData
import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.notifications.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotificationsPager(): Flow<PagingData<Notification>>
    suspend fun markAsRead(notificationId: String): Result<Unit, AppError>
    suspend fun markAllAsRead(): Result<Unit, AppError>
}