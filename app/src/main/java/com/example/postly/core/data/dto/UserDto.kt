package com.example.postly.core.data.dto

import com.example.postly.core.domain.model.User

data class UserDto(
    val id: String? = null,
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val photo: String? = null,
    val followers: Int = 0,
    val following: Int = 0
)

fun UserDto.toDomain(): User = User(
    id = id.orEmpty(),
    name = name,
    username = username,
    email = email,
    photo = photo,
    followers = followers,
    following = following
)