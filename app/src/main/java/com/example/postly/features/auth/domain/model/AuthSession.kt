package com.example.postly.features.auth.domain.model

import com.example.postly.core.domain.model.User

data class AuthSession(
    val token: String,
    val user: User
)