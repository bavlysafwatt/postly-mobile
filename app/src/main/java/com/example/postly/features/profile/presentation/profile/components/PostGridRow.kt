package com.example.postly.features.profile.presentation.profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.postly.core.components.PostGridItem
import com.example.postly.core.domain.model.Post
import kotlin.collections.forEach

@Composable
fun PostGridRow(row: List<Post>, onPostClick: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        row.forEach { post ->
            PostGridItem(
                post = post,
                onClick = { onPostClick(post.id) },
                modifier = Modifier.weight(1f)
            )
        }
        repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
    }
}