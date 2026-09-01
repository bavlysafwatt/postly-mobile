package com.example.postly.features.auth.data.dto

import com.example.postly.core.data.dto.UserDto


data class AuthEnvelope(
    val accessToken: String,
    val user: UserDto
)