package com.example.postly.features.profile.presentation.userlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.postly.core.components.ShimmerBone
import com.valentinilk.shimmer.shimmer

@Composable
fun FollowRowShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerBone(modifier = Modifier.size(48.dp), shape = CircleShape)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            ShimmerBone(modifier = Modifier
                .width(120.dp)
                .height(14.dp))
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBone(modifier = Modifier
                .width(80.dp)
                .height(12.dp))
        }
    }
}