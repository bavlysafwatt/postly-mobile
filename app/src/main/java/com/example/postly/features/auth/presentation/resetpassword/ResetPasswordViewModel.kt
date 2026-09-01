package com.example.postly.features.auth.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.auth.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onOtpChange(value: String) = _uiState.update { it.copy(otp = value, otpError = null) }
    fun onNewPasswordChange(value: String) =
        _uiState.update { it.copy(newPassword = value, newPasswordError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }

    fun resetPassword() {
        val state = _uiState.value
        val otpError = if (state.otp.length != 6) "Enter the 6-digit code" else null
        val newPasswordError =
            if (state.newPassword.length < 6) "Password must be at least 6 characters" else null
        val confirmPasswordError =
            if (state.confirmPassword != state.newPassword) "Passwords don't match" else null

        if (otpError != null || newPasswordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    otpError = otpError,
                    newPasswordError = newPasswordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result =
                resetPasswordUseCase(state.otp, state.newPassword, state.confirmPassword)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        resetSuccess = true
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
}