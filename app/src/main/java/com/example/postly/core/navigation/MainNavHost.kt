package com.example.postly.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.features.feed.presentation.createoredit.CreateOrEditPostScreen
import com.example.postly.features.feed.presentation.feed.FeedScreen
import com.example.postly.features.feed.presentation.postdetail.PostDetailScreen
import com.example.postly.features.notifications.presentation.NotificationsScreen
import com.example.postly.features.profile.presentation.editprofile.EditProfileScreen
import com.example.postly.features.profile.presentation.profile.ProfileScreen
import com.example.postly.features.profile.presentation.userlist.FollowListMode
import com.example.postly.features.profile.presentation.userlist.FollowListScreen
import com.example.postly.features.search.presentation.SearchScreen
import com.example.postly.features.settings.presentation.changepassword.ChangePasswordScreen
import com.example.postly.features.settings.presentation.settings.SettingsScreen

@Composable
fun MainNavHost(
    currentUserCache: CurrentUserCache,
    onLoggedOut: () -> Unit
) {
    val navController = rememberNavController()
    val currentUser by currentUserCache.observeUser()
        .collectAsStateWithLifecycle(initialValue = null)

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
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
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
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Route.Home> {
                FeedScreen(
                    onPostClick = { postId -> navController.navigate(Route.PostDetail(postId)) },
                    onCreatePost = { navController.navigate(Route.CreateOrEditPost()) }
                )
            }
            composable<Route.Search> {
                SearchScreen(onUserClick = { userId ->
                    navController.navigate(
                        Route.UserProfile(
                            userId
                        )
                    )
                })
            }
            composable<Route.Notifications> {
                NotificationsScreen(
                    onNavigateToPostDetail = { postId ->
                        navController.navigate(
                            Route.PostDetail(
                                postId
                            )
                        )
                    },
                    onNavigateToUserProfile = { userId ->
                        navController.navigate(
                            Route.UserProfile(
                                userId
                            )
                        )
                    }
                )
            }
            composable<Route.Profile> {
                ProfileScreen(
                    userId = null,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSettings = { navController.navigate(Route.Settings) },
                    onNavigateToEditProfile = { navController.navigate(Route.EditProfile) },
                    onNavigateToFollowers = { userId ->
                        navController.navigate(
                            Route.Followers(
                                userId
                            )
                        )
                    },
                    onNavigateToFollowing = { userId ->
                        navController.navigate(
                            Route.Following(
                                userId
                            )
                        )
                    },
                    onPostClick = { postId -> navController.navigate(Route.PostDetail(postId)) }
                )
            }
            composable<Route.UserProfile> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.UserProfile>()
                ProfileScreen(
                    userId = args.userId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSettings = { navController.navigate(Route.Settings) },
                    onNavigateToEditProfile = { navController.navigate(Route.EditProfile) },
                    onNavigateToFollowers = { userId ->
                        navController.navigate(
                            Route.Followers(
                                userId
                            )
                        )
                    },
                    onNavigateToFollowing = { userId ->
                        navController.navigate(
                            Route.Following(
                                userId
                            )
                        )
                    },
                    onPostClick = { postId -> navController.navigate(Route.PostDetail(postId)) }
                )
            }
            composable<Route.PostDetail> {
                PostDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onEditPost = { postId -> navController.navigate(Route.CreateOrEditPost(postId)) }
                )
            }
            composable<Route.CreateOrEditPost> {
                CreateOrEditPostScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable<Route.Followers> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.Followers>()
                FollowListScreen(
                    userId = args.userId,
                    mode = FollowListMode.FOLLOWERS,
                    onNavigateBack = { navController.popBackStack() },
                    onUserClick = { userId -> navController.navigate(Route.UserProfile(userId)) }
                )
            }
            composable<Route.Following> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.Following>()
                FollowListScreen(
                    userId = args.userId,
                    mode = FollowListMode.FOLLOWING,
                    onNavigateBack = { navController.popBackStack() },
                    onUserClick = { userId -> navController.navigate(Route.UserProfile(userId)) }
                )
            }
            composable<Route.EditProfile> {
                EditProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToChangePassword = { navController.navigate(Route.ChangePassword) },
                    onLoggedOut = onLoggedOut
                )
            }
            composable<Route.ChangePassword> {
                ChangePasswordScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
        }
    }
}