package com.example.postly.core.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer

@Composable
fun PostGridItemShimmer(modifier: Modifier = Modifier) {
    ShimmerBone(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .shimmer()
    )
}