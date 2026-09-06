package com.example.postly.features.profile.data.dto

import com.example.postly.core.data.dto.UserSummaryDto
import com.google.gson.annotations.SerializedName


data class FollowActionDto(
    @SerializedName("_id")
    val id: String? = null
)

data class FollowEnvelope(val follow: FollowActionDto)

data class FollowerEntryDto(
    @SerializedName("_id")
    val id: String? = null, val follower: UserSummaryDto? = null
)

data class FollowersEnvelope(val followers: List<FollowerEntryDto> = emptyList())

data class FollowingEntryDto(
    @SerializedName("_id")
    val id: String? = null, val following: UserSummaryDto? = null
)

data class FollowingEnvelope(val following: List<FollowingEntryDto> = emptyList())