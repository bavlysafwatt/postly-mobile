package com.example.postly.features.profile.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val photo: String?,
    val followers: Int,
    val following: Int,
    val isFollowing: Boolean?
)