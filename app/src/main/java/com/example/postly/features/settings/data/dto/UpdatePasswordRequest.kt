package com.example.postly.features.settings.data.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val passwordConfirm: String
)