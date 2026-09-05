package com.example.postly.features.notifications.data.dto

import com.example.postly.core.data.dto.UserSummaryDto
import com.example.postly.core.data.dto.toDomain
import com.example.postly.features.notifications.domain.model.Notification
import com.example.postly.features.notifications.domain.model.NotificationType
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("_id")
    val id: String? = null,
    val sender: UserSummaryDto? = null,
    val type: String = "",
    val post: String? = null,
    val isRead: Boolean = false,
    val createdAt: String? = null
)

fun NotificationDto.toDomain(): Notification = Notification(
    id = id.orEmpty(),
    sender = sender?.toDomain(),
    type = when (type) {
        "like" -> NotificationType.LIKE
        "comment" -> NotificationType.COMMENT
        "follow" -> NotificationType.FOLLOW
        else -> NotificationType.UNKNOWN
    },
    postId = post,
    isRead = isRead,
    createdAt = createdAt.orEmpty()
)