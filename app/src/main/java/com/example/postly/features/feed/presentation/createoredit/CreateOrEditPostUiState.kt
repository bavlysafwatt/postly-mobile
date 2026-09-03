package com.example.postly.features.feed.presentation.createoredit

import android.net.Uri

data class CreateOrEditPostUiState(
    val postId: String? = null,
    val content: String = "",
    val contentError: String? = null,
    val existingPhotoUrls: List<String> = emptyList(),
    val newPhotoUris: List<Uri> = emptyList(),
    val isPhotosChanged: Boolean = false,
    val isLoadingExistingPost: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
) {
    val isEditMode: Boolean get() = postId != null
}