package com.example.postly.features.notifications.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.postly.core.common.Constants
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.map
import com.example.postly.core.network.AppError
import com.example.postly.core.network.safeApiCall
import com.example.postly.core.network.safeApiCallUnit
import com.example.postly.features.notifications.data.paging.NotificationsPagingSource
import com.example.postly.features.notifications.data.remote.NotificationApi
import com.example.postly.features.notifications.domain.model.Notification
import com.example.postly.features.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
) : NotificationRepository {

    override fun getNotificationsPager(): Flow<PagingData<Notification>> =
        Pager(PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)) { NotificationsPagingSource(api) }.flow

    override suspend fun markAsRead(notificationId: String): Result<Unit, AppError> =
        safeApiCall { api.markAsRead(notificationId) }.map { }

    override suspend fun markAllAsRead(): Result<Unit, AppError> =
        safeApiCallUnit { api.markAllAsRead() }
}