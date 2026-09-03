package com.example.postly.features.feed.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.postly.core.domain.Result
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.domain.model.Post
import com.example.postly.core.navigation.Route
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.feed.domain.model.Comment
import com.example.postly.features.feed.domain.usecase.AddCommentUseCase
import com.example.postly.features.feed.domain.usecase.BookmarkPostUseCase
import com.example.postly.features.feed.domain.usecase.DeletePostUseCase
import com.example.postly.features.feed.domain.usecase.GetCommentsUseCase
import com.example.postly.features.feed.domain.usecase.GetPostUseCase
import com.example.postly.features.feed.domain.usecase.LikePostUseCase
import com.example.postly.features.feed.domain.usecase.UnbookmarkPostUseCase
import com.example.postly.features.feed.domain.usecase.UnlikePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostUseCase: GetPostUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unbookmarkPostUseCase: UnbookmarkPostUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    getCommentsUseCase: GetCommentsUseCase,
    private val currentUserCache: CurrentUserCache
) : ViewModel() {

    val postId: String = savedStateHandle.toRoute<Route.PostDetail>().postId

    val commentsFlow: Flow<PagingData<Comment>> =
        getCommentsUseCase(postId).cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private val _refreshComments = MutableSharedFlow<Unit>()
    val refreshComments: SharedFlow<Unit> = _refreshComments.asSharedFlow()

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentUser = currentUserCache.observeUser().first()
            when (val result = getPostUseCase(postId)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        post = result.data,
                        isLoading = false,
                        isCurrentUserAuthor = result.data.author?.id == currentUser?.id
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toUserMessage())
                }
            }
        }
    }

    fun toggleLike() {
        val post = _uiState.value.post ?: return
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else (post.likeCount - 1).coerceAtLeast(0)
        _uiState.update { it.copy(post = post.copy(isLiked = newLiked, likeCount = newCount)) }

        viewModelScope.launch {
            val result = if (newLiked) likePostUseCase(postId) else unlikePostUseCase(postId)
            if (result is Result.Failure) {
                _uiState.update { it.copy(post = post) }
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }

    fun toggleBookmark() {
        val post = _uiState.value.post ?: return
        val newBookmarked = !post.isBookmarked
        _uiState.update { it.copy(post = post.copy(isBookmarked = newBookmarked)) }

        viewModelScope.launch {
            val result =
                if (newBookmarked) bookmarkPostUseCase(postId) else unbookmarkPostUseCase(postId)
            if (result is Result.Failure) {
                _uiState.update { it.copy(post = post) }
                _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }

    fun onCommentTextChange(value: String) = _uiState.update { it.copy(commentText = value) }

    fun submitComment() {
        val content = _uiState.value.commentText.trim()
        if (content.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingComment = true) }
            when (val result = addCommentUseCase(postId, content)) {
                is Result.Success -> {
                    val post = _uiState.value.post
                    _uiState.update {
                        it.copy(
                            isSubmittingComment = false,
                            commentText = "",
                            post = post?.copy(commentCount = post.commentCount + 1)
                        )
                    }
                    _refreshComments.emit(Unit)
                }

                is Result.Failure -> {
                    _uiState.update { it.copy(isSubmittingComment = false) }
                    _errorEvent.emit(result.error.toUserMessage())
                }
            }
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            when (val result = deletePostUseCase(postId)) {
                is Result.Success -> _uiState.update { it.copy(postDeleted = true) }
                is Result.Failure -> _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }
}