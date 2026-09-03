package com.example.postly.features.feed.presentation.postdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.postly.core.components.Avatar
import com.example.postly.features.feed.domain.model.Comment

@Composable
fun CommentRow(comment: Comment) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Avatar(photoUrl = comment.user?.photo, name = comment.user?.name ?: "?", size = 32.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = comment.user?.username ?: "unknown",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(text = comment.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}