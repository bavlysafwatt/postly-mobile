package com.example.postly.features.notifications.domain.usecase

import androidx.paging.PagingData
import com.example.postly.features.notifications.domain.model.Notification
import com.example.postly.features.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> = repository.getNotificationsPager()
}