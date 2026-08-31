package com.example.postly.core.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.postly.core.components.Avatar
import com.example.postly.core.domain.model.User

private data class BottomNavItem(val route: Route, val label: String)

private val bottomNavItems = listOf(
    BottomNavItem(Route.Home, "Home"),
    BottomNavItem(Route.Search, "Search"),
    BottomNavItem(Route.Notifications, "Notifications"),
    BottomNavItem(Route.Profile, "Profile")
)

@Composable
fun BottomNavBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
    currentUser: User?
) {
    NavigationBar(modifier = Modifier.height(70.dp)) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    when (item.route) {
                        Route.Home -> Icon(Icons.Rounded.Home, contentDescription = item.label)
                        Route.Search -> Icon(Icons.Rounded.Search, contentDescription = item.label)
                        Route.Notifications -> Icon(Icons.Rounded.Notifications, contentDescription = item.label)
                        Route.Profile -> Avatar(
                            photoUrl = currentUser?.photo,
                            name = currentUser?.name ?: "?",
                            size = 24.dp
                        )
                        else -> Unit
                    }
                },
            )
        }
    }
}