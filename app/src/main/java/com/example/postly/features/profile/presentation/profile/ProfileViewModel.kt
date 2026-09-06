package com.example.postly.features.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.profile.domain.usecase.FollowUserUseCase
import com.example.postly.features.profile.domain.usecase.GetMyBookmarksUseCase
import com.example.postly.features.profile.domain.usecase.GetMyProfileUseCase
import com.example.postly.features.profile.domain.usecase.GetUserPostsUseCase
import com.example.postly.features.profile.domain.usecase.GetUserProfileUseCase
import com.example.postly.features.profile.domain.usecase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getMyBookmarksUseCase: GetMyBookmarksUseCase,
    private val currentUserCache: CurrentUserCache
) : ViewModel() {

    private val _internalState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> =
        combine(_internalState, currentUserCache.observeUser()) { state, cachedUser ->
            val profile = state.profile
            if (state.isOwnProfile && profile != null && cachedUser != null) {
                state.copy(
                    profile = profile.copy(
                        name = cachedUser.name,
                        photo = cachedUser.photo,
                        email = cachedUser.email,
                        followers = cachedUser.followers,
                        following = cachedUser.following
                    )
                )
            } else {
                state
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private var loadedFor: String? = "unset"

    fun load(userId: String?) {
        val key = userId ?: "self"
        if (loadedFor == key) return
        loadedFor = key

        viewModelScope.launch {
            val currentUser = currentUserCache.observeUser().first()
            val isOwn = userId == null || userId == currentUser?.id
            _internalState.value =
                ProfileUiState(resolvedUserId = if (isOwn) null else userId, isOwnProfile = isOwn)
            loadProfile()
            loadPosts(reset = true)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadProfile()
            loadPosts(reset = true)
            if (_internalState.value.selectedTab == ProfileTab.BOOKMARKS) loadBookmarks()
        }
    }

    private suspend fun loadProfile() {
        _internalState.update { it.copy(isLoadingProfile = true, profileError = null) }
        val userId = _internalState.value.resolvedUserId
        val result = if (userId == null) getMyProfileUseCase() else getUserProfileUseCase(userId)
        when (result) {
            is Result.Success -> _internalState.update {
                it.copy(
                    profile = result.data,
                    isLoadingProfile = false
                )
            }

            is Result.Failure -> _internalState.update {
                it.copy(
                    isLoadingProfile = false,
                    profileError = result.error.toUserMessage()
                )
            }
        }
    }

    fun selectTab(tab: ProfileTab) {
        _internalState.update { it.copy(selectedTab = tab) }
        if (tab == ProfileTab.BOOKMARKS && _internalState.value.bookmarks.isEmpty() && !_internalState.value.isLoadingBookmarks) {
            loadBookmarks()
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            _internalState.update { it.copy(isLoadingBookmarks = true, bookmarksError = null) }
            when (val result = getMyBookmarksUseCase()) {
                is Result.Success -> _internalState.update {
                    it.copy(
                        bookmarks = result.data,
                        isLoadingBookmarks = false
                    )
                }

                is Result.Failure -> _internalState.update {
                    it.copy(
                        isLoadingBookmarks = false,
                        bookmarksError = result.error.toUserMessage()
                    )
                }
            }
        }
    }

    fun loadPosts(reset: Boolean = false) {
        viewModelScope.launch {
            val page = if (reset) 1 else _internalState.value.postsPage
            _internalState.update {
                if (reset) it.copy(
                    isLoadingPosts = true,
                    postsError = null
                ) else it.copy(isLoadingMorePosts = true)
            }
            when (val result = getUserPostsUseCase(_internalState.value.resolvedUserId, page)) {
                is Result.Success -> _internalState.update {
                    it.copy(
                        posts = if (reset) result.data.posts else it.posts + result.data.posts,
                        postsPage = page + 1,
                        postsTotalCount = result.data.totalResults,
                        postsHasNextPage = result.data.hasNextPage,
                        isLoadingPosts = false,
                        isLoadingMorePosts = false
                    )
                }

                is Result.Failure -> _internalState.update {
                    it.copy(
                        isLoadingPosts = false,
                        isLoadingMorePosts = false,
                        postsError = result.error.toUserMessage()
                    )
                }
            }
        }
    }

    fun loadMorePostsIfNeeded() {
        val state = _internalState.value
        if (state.isLoadingMorePosts || state.isLoadingPosts || !state.postsHasNextPage) return
        loadPosts(reset = false)
    }

    fun toggleFollow() {
        val profile = _internalState.value.profile ?: return
        val isFollowing = profile.isFollowing ?: return
        val newFollowing = !isFollowing
        val newFollowerCount =
            if (newFollowing) profile.followers + 1 else (profile.followers - 1).coerceAtLeast(0)

        _internalState.update {
            it.copy(
                profile = profile.copy(
                    isFollowing = newFollowing,
                    followers = newFollowerCount
                ), isFollowActionInProgress = true
            )
        }

        viewModelScope.launch {
            val result =
                if (newFollowing) followUserUseCase(profile.id) else unfollowUserUseCase(profile.id)
            _internalState.update { it.copy(isFollowActionInProgress = false) }
            if (result is Result.Failure) {
                _internalState.update { it.copy(profile = profile) }
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }
}