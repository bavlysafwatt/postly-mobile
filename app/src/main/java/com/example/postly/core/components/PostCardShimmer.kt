package com.example.postly.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun PostCardShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            ShimmerBone(modifier = Modifier.size(38.dp), shape = CircleShape)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                ShimmerBone(modifier = Modifier.width(120.dp).height(14.dp))
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBone(modifier = Modifier.width(80.dp).height(12.dp))
            }
        }

        ShimmerBone(modifier = Modifier.fillMaxWidth().aspectRatio(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBone(modifier = Modifier.size(24.dp), shape = CircleShape)
            Spacer(modifier = Modifier.width(6.dp))
            ShimmerBone(modifier = Modifier.width(18.dp).height(12.dp))

            Spacer(modifier = Modifier.width(16.dp))

            ShimmerBone(modifier = Modifier.size(24.dp), shape = CircleShape)
            Spacer(modifier = Modifier.width(6.dp))
            ShimmerBone(modifier = Modifier.width(18.dp).height(12.dp))

            Spacer(modifier = Modifier.weight(1f))
            ShimmerBone(modifier = Modifier.size(24.dp), shape = CircleShape)
        }

        ShimmerBone(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(0.8f).height(12.dp))
        Spacer(modifier = Modifier.height(6.dp))

        ShimmerBone(modifier = Modifier.padding(horizontal = 16.dp).width(60.dp).height(10.dp))
    }
}