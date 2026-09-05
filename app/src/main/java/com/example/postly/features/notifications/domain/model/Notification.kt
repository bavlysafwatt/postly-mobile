package com.example.postly.features.notifications.domain.model

import com.example.postly.core.domain.model.UserSummary

enum class NotificationType { LIKE, COMMENT, FOLLOW, UNKNOWN }

data class Notification(
    val id: String,
    val sender: UserSummary?,
    val type: NotificationType,
    val postId: String?,
    val isRead: Boolean,
    val createdAt: String
)