package com.example.postly.features.profile.presentation.editprofile

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.profile.domain.usecase.GetMyProfileUseCase
import com.example.postly.features.profile.domain.usecase.UpdateMeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateMeUseCase: UpdateMeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = getMyProfileUseCase()) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = result.data.name,
                        email = result.data.email,
                        currentPhotoUrl = result.data.photo
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, nameError = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, emailError = null) }
    fun onPhotoSelected(uri: Uri?) = _uiState.update { it.copy(newPhotoUri = uri) }

    fun save() {
        val state = _uiState.value
        val nameError =
            if (state.name.trim().length < 3) "Name must be at least 3 characters" else null
        val emailError = if (!Patterns.EMAIL_ADDRESS.matcher(state.email)
                .matches()
        ) "Enter a valid email" else null

        if (nameError != null || emailError != null) {
            _uiState.update { it.copy(nameError = nameError, emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val result =
                updateMeUseCase(state.name.trim(), state.email.trim(), state.newPhotoUri)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }
}