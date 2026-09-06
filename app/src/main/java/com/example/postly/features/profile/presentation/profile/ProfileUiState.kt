package com.example.postly.features.profile.presentation.profile

import com.example.postly.core.domain.model.Post
import com.example.postly.features.profile.domain.model.UserProfile

enum class ProfileTab { POSTS, BOOKMARKS }

data class ProfileUiState(
    val resolvedUserId: String? = null,
    val profile: UserProfile? = null,
    val isOwnProfile: Boolean = true,
    val isLoadingProfile: Boolean = true,
    val profileError: String? = null,
    val isFollowActionInProgress: Boolean = false,
    val selectedTab: ProfileTab = ProfileTab.POSTS,
    val posts: List<Post> = emptyList(),
    val postsPage: Int = 1,
    val postsTotalCount: Int = 0,
    val postsHasNextPage: Boolean = true,
    val isLoadingPosts: Boolean = true,
    val isLoadingMorePosts: Boolean = false,
    val postsError: String? = null,
    val bookmarks: List<Post> = emptyList(),
    val isLoadingBookmarks: Boolean = false,
    val bookmarksError: String? = null
)