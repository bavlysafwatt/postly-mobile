package com.example.postly.features.auth.data.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val passwordConfirm: String
)