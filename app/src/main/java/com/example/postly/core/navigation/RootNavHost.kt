package com.example.postly.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.postly.core.data.local.CurrentUserCache

@Composable
private fun PlaceholderScreen(label: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = label, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun RootNavHost(currentUserCache: CurrentUserCache) {
    val navController = rememberNavController()
    val currentUser by currentUserCache.observeUser().collectAsStateWithLifecycle(initialValue = null)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    val currentTab: Route? = when {
        destination?.hasRoute<Route.Home>() == true -> Route.Home
        destination?.hasRoute<Route.Search>() == true -> Route.Search
        destination?.hasRoute<Route.Notifications>() == true -> Route.Notifications
        destination?.hasRoute<Route.Profile>() == true -> Route.Profile
        else -> null
    }

    Scaffold(
        bottomBar = {
            if (currentTab != null) {
                BottomNavBar(
                    currentRoute = currentTab,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentUser = currentUser
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Route.Splash> { PlaceholderScreen("Splash") }
            composable<Route.Onboarding> { PlaceholderScreen("Onboarding") }
            composable<Route.Auth.Login> { PlaceholderScreen("Login") }
            composable<Route.Auth.Register> { PlaceholderScreen("Register") }
            composable<Route.Auth.ForgotPassword> { PlaceholderScreen("Forgot password") }
            composable<Route.Auth.ResetPassword> { PlaceholderScreen("Reset password") }

            composable<Route.Home> { PlaceholderScreen("Home / Feed") }
            composable<Route.Search> { PlaceholderScreen("Search") }
            composable<Route.Notifications> { PlaceholderScreen("Notifications") }
            composable<Route.Profile> { PlaceholderScreen("Profile") }

            composable<Route.UserProfile> { PlaceholderScreen("User profile") }
            composable<Route.PostDetail> { PlaceholderScreen("Post detail") }
            composable<Route.CreateOrEditPost> { PlaceholderScreen("Create/edit post") }
            composable<Route.Followers> { PlaceholderScreen("Followers") }
            composable<Route.Following> { PlaceholderScreen("Following") }
            composable<Route.EditProfile> { PlaceholderScreen("Edit profile") }
            composable<Route.Settings> { PlaceholderScreen("Settings") }
        }
    }
}