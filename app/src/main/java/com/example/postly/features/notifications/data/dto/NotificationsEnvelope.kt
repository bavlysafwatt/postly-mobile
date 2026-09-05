package com.example.postly.features.notifications.data.dto

data class NotificationsEnvelope(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val notifications: List<NotificationDto>
)