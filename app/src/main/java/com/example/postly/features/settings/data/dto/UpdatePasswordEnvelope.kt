package com.example.postly.features.settings.data.dto

import com.example.postly.core.data.dto.UserDto

data class UpdatePasswordEnvelope(
    val accessToken: String,
    val user: UserDto
)