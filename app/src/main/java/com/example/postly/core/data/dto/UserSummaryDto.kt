package com.example.postly.core.data.dto
import com.example.postly.core.domain.model.UserSummary

data class UserSummaryDto(
    val id: String? = null,
    val name: String = "",
    val username: String = "",
    val photo: String? = null
)

fun UserSummaryDto.toDomain(): UserSummary = UserSummary(
    id = id.orEmpty(),
    name = name,
    username = username,
    photo = photo
)