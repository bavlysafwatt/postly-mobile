package com.example.postly.features.profile.presentation.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import com.example.postly.features.profile.presentation.profile.ProfileTab

@Composable
fun ProfileTabRow(selectedTab: ProfileTab, onTabSelected: (ProfileTab) -> Unit) {
    TabRow(selectedTabIndex = selectedTab.ordinal) {
        Tab(
            selected = selectedTab == ProfileTab.POSTS,
            onClick = { onTabSelected(ProfileTab.POSTS) },
            icon = { Icon(Icons.Rounded.GridOn, contentDescription = "Posts") }
        )
        Tab(
            selected = selectedTab == ProfileTab.BOOKMARKS,
            onClick = { onTabSelected(ProfileTab.BOOKMARKS) },
            icon = { Icon(Icons.Rounded.Bookmarks, contentDescription = "Bookmarks") }
        )
    }
}