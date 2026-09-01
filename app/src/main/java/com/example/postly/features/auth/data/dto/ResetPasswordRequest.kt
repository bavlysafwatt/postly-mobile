package com.example.postly.features.auth.data.dto

data class ResetPasswordRequest(
    val otp: String,
    val newPassword: String,
    val passwordConfirm: String
)