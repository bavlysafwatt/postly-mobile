package com.example.postly.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.postly.core.domain.model.UserSummary

@Composable
fun SearchResults(
    users: List<UserSummary>,
    onUserClick: (UserSummary) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserSearchRow(
                user = user,
                onClick = { onUserClick(user) }
            )
        }
    }
}