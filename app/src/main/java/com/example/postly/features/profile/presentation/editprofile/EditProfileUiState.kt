package com.example.postly.features.profile.presentation.editprofile

import android.net.Uri

data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val currentPhotoUrl: String? = null,
    val newPhotoUri: Uri? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)