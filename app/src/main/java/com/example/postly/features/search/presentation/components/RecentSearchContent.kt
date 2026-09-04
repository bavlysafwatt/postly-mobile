package com.example.postly.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.postly.features.search.domain.model.RecentSearch

@Composable
fun RecentSearchContent(
    searches: List<RecentSearch>,
    onSearchClick: (RecentSearch) -> Unit,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit
) {
    if (searches.isEmpty()) {
        EmptySearchHistory()
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onClearAll) {
                Text("Clear all")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = searches,
                key = { it.userId }
            ) { search ->
                RecentSearchRow(
                    search = search,
                    onClick = { onSearchClick(search) },
                    onRemove = { onRemove(search.userId) }
                )
            }
        }
    }
}