package com.example.postly.features.notifications.domain.usecase

import com.example.postly.core.domain.Result
import com.example.postly.core.network.AppError
import com.example.postly.features.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkAllAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit, AppError> = repository.markAllAsRead()
}