package com.example.postly.core.domain.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val photo: String?,
    val followers: Int,
    val following: Int
)