package com.example.postly.features.profile.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.postly.core.components.Avatar
import com.example.postly.core.components.PostlyButton
import com.example.postly.features.profile.domain.model.UserProfile

@Composable
fun ProfileHeader(
    profile: UserProfile,
    isOwnProfile: Boolean,
    isFollowActionInProgress: Boolean,
    postsCount: Int,
    onEditProfile: () -> Unit,
    onToggleFollow: () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(photoUrl = profile.photo, name = profile.name, size = 88.dp)
            Spacer(modifier = Modifier.width(20.dp))
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatColumn(count = postsCount, label = "Posts")
                StatColumn(
                    count = profile.followers,
                    label = "Followers",
                    onClick = onFollowersClick
                )
                StatColumn(
                    count = profile.following,
                    label = "Following",
                    onClick = onFollowingClick
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            profile.name,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            "@${profile.username}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(14.dp))

        if (isOwnProfile) {
            OutlinedButton(
                onClick = onEditProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Edit profile", fontWeight = FontWeight.SemiBold) }
        } else {
            val isFollowing = profile.isFollowing == true
            if (isFollowing) {
                OutlinedButton(
                    onClick = onToggleFollow,
                    enabled = !isFollowActionInProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Following", fontWeight = FontWeight.SemiBold) }
            } else {
                PostlyButton(
                    text = "Follow",
                    onClick = onToggleFollow,
                    loading = isFollowActionInProgress
                )
            }
        }
    }
}