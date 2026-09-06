package com.example.postly.features.settings.presentation.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.settings.domain.usecase.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(value: String) =
        _uiState.update { it.copy(currentPassword = value, currentPasswordError = null) }

    fun onNewPasswordChange(value: String) =
        _uiState.update { it.copy(newPassword = value, newPasswordError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }

    fun submit() {
        val state = _uiState.value
        val currentError =
            if (state.currentPassword.length < 6) "Enter your current password" else null
        val newError =
            if (state.newPassword.length < 6) "Password must be at least 6 characters" else null
        val confirmError =
            if (state.confirmPassword != state.newPassword) "Passwords don't match" else null

        if (currentError != null || newError != null || confirmError != null) {
            _uiState.update {
                it.copy(
                    currentPasswordError = currentError,
                    newPasswordError = newError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            when (val result = changePasswordUseCase(
                state.currentPassword,
                state.newPassword,
                state.confirmPassword
            )) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        success = true
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