package com.example.postly.features.profile.data.dto

import com.example.postly.features.profile.domain.model.UserProfile
import com.google.gson.annotations.SerializedName

/** isFollowing is present (non-null) only from GET /users/:id, always null from GET /users/me
 *  and update-me -- that's how we tell "own profile" apart from "someone else's". */
data class UserProfileDto(
    @SerializedName("_id")
    val id: String? = null,
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val photo: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    val isFollowing: Boolean? = null
)

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id.orEmpty(),
    name = name,
    username = username,
    email = email,
    photo = photo,
    followers = followers,
    following = following,
    isFollowing = isFollowing
)

data class UserProfileEnvelope(val user: UserProfileDto)