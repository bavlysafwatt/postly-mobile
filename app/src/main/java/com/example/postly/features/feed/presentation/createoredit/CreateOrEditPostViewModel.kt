package com.example.postly.features.feed.presentation.createoredit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.postly.core.domain.Result
import com.example.postly.core.navigation.Route
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.feed.domain.usecase.CreatePostUseCase
import com.example.postly.features.feed.domain.usecase.GetPostUseCase
import com.example.postly.features.feed.domain.usecase.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrEditPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostUseCase: GetPostUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase
) : ViewModel() {

    private val postId: String? = savedStateHandle.toRoute<Route.CreateOrEditPost>().postId

    private val _uiState = MutableStateFlow(CreateOrEditPostUiState(postId = postId))
    val uiState: StateFlow<CreateOrEditPostUiState> = _uiState.asStateFlow()

    init {
        postId?.let { loadExistingPost(it) }
    }

    private fun loadExistingPost(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingExistingPost = true) }
            when (val result = getPostUseCase(id)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoadingExistingPost = false,
                        content = result.data.content,
                        existingPhotoUrls = result.data.photos
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isLoadingExistingPost = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }

    fun onContentChange(value: String) =
        _uiState.update { it.copy(content = value, contentError = null) }

    fun onPhotosPicked(uris: List<Uri>) {
        _uiState.update {
            it.copy(
                newPhotoUris = (it.newPhotoUris + uris).distinct().take(5),
                isPhotosChanged = true
            )
        }
    }

    fun removeNewPhoto(uri: Uri) = _uiState.update { it.copy(newPhotoUris = it.newPhotoUris - uri) }

    fun clearExistingPhotosForReplace() =
        _uiState.update { it.copy(isPhotosChanged = true, existingPhotoUrls = emptyList()) }

    fun save() {
        val state = _uiState.value
        if (state.content.trim().length < 10) {
            _uiState.update { it.copy(contentError = "Write at least 10 characters") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val result = if (state.isEditMode) {
                updatePostUseCase(
                    state.postId!!,
                    state.content.trim(),
                    if (state.isPhotosChanged) state.newPhotoUris else null
                )
            } else {
                createPostUseCase(state.content.trim(), state.newPhotoUris)
            }

            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        saveSuccess = true
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }
}