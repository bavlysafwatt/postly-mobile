package com.example.postly.features.search.data.dto

import com.example.postly.core.data.dto.UserSummaryDto

data class SearchUsersEnvelope(
    val users: List<UserSummaryDto> = emptyList()
)