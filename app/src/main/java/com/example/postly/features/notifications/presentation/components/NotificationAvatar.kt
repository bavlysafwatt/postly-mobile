package com.example.postly.features.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.postly.core.components.Avatar
import com.example.postly.features.notifications.domain.model.Notification
import com.example.postly.features.notifications.domain.model.NotificationType

@Composable
fun NotificationAvatar(notification: Notification) {
    Box {
        Avatar(
            photoUrl = notification.sender?.photo,
            name = notification.sender?.name ?: "?",
            size = 48.dp
        )

        val (badgeColor, badgeIcon) = when (notification.type) {
            NotificationType.LIKE -> MaterialTheme.colorScheme.error to Icons.Rounded.Favorite
            NotificationType.COMMENT -> MaterialTheme.colorScheme.primary to Icons.Rounded.ChatBubble
            NotificationType.FOLLOW -> MaterialTheme.colorScheme.tertiary to Icons.Rounded.PersonAdd
            NotificationType.UNKNOWN -> MaterialTheme.colorScheme.outline to Icons.Rounded.Notifications
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(20.dp)
                .clip(CircleShape)
                .background(badgeColor)
                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = badgeIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}
