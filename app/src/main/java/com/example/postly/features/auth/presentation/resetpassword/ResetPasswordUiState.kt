package com.example.postly.features.auth.presentation.resetpassword

data class ResetPasswordUiState(
    val otp: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val otpError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val resetSuccess: Boolean = false
)