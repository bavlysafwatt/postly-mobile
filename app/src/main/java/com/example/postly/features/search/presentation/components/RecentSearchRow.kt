package com.example.postly.features.search.presentation.components

import androidx.compose.runtime.Composable
import com.example.postly.features.search.domain.model.RecentSearch

@Composable
fun RecentSearchRow(
    search: RecentSearch,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    RecentSearchRowContent(
        search = search,
        onClick = onClick,
        onRemove = onRemove
    )
}
