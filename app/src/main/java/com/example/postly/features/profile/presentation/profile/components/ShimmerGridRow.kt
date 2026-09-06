package com.example.postly.features.profile.presentation.profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.postly.core.components.PostGridItemShimmer

@Composable
fun ShimmerGridRow() {
    Row(modifier = Modifier.fillMaxWidth()) {
        repeat(3) { PostGridItemShimmer(modifier = Modifier.weight(1f)) }
    }
}