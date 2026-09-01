package com.example.postly.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.valentinilk.shimmer.shimmer

@Composable
fun Avatar(
    photoUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    if (photoUrl.isNullOrBlank()) {
        AvatarPlaceholder(
            name = name,
            modifier = modifier,
            size = size
        )
    } else {
        SubcomposeAsyncImage(
            model = photoUrl,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape),

            loading = {
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .shimmer()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            },

            error = {
                AvatarPlaceholder(
                    name = name,
                    size = size
                )
            }
        )
    }
}

@Composable
private fun AvatarPlaceholder(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}