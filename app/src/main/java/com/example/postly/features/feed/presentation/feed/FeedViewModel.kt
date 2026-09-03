package com.example.postly.features.feed.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.postly.core.domain.Result
import com.example.postly.core.domain.model.Post
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.feed.domain.usecase.BookmarkPostUseCase
import com.example.postly.features.feed.domain.usecase.GetFeedUseCase
import com.example.postly.features.feed.domain.usecase.LikePostUseCase
import com.example.postly.features.feed.domain.usecase.UnbookmarkPostUseCase
import com.example.postly.features.feed.domain.usecase.UnlikePostUseCase
import com.example.postly.features.feed.domain.model.PostOverride
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getFeedUseCase: GetFeedUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase
) : ViewModel() {

    val feed: Flow<PagingData<Post>> = getFeedUseCase().cachedIn(viewModelScope)

    private val _overrides = MutableStateFlow<Map<String, PostOverride>>(emptyMap())
    val overrides: StateFlow<Map<String, PostOverride>> = _overrides.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    fun toggleLike(post: Post) {
        val current = _overrides.value[post.id] ?: PostOverride(
            post.isLiked,
            post.isBookmarked,
            post.likeCount
        )
        val newLiked = !current.isLiked
        val newCount =
            if (newLiked) current.likeCount + 1 else (current.likeCount - 1).coerceAtLeast(0)
        _overrides.update {
            it + (post.id to current.copy(
                isLiked = newLiked,
                likeCount = newCount
            ))
        }

        viewModelScope.launch {
            val result = if (newLiked) likePostUseCase(post.id) else unlikePostUseCase(post.id)
            if (result is Result.Failure) {
                _overrides.update { it + (post.id to current) }
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }

    fun toggleBookmark(post: Post) {
        val current = _overrides.value[post.id] ?: PostOverride(
            post.isLiked,
            post.isBookmarked,
            post.likeCount
        )
        val newBookmarked = !current.isBookmarked
        _overrides.update { it + (post.id to current.copy(isBookmarked = newBookmarked)) }

        viewModelScope.launch {
            val result =
                if (newBookmarked) bookmarkPostUseCase(post.id) else unbookmarkPostUseCase(post.id)
            if (result is Result.Failure) {
                _overrides.update { it + (post.id to current) }
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }
}