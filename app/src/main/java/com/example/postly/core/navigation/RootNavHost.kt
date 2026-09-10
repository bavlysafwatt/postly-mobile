package com.example.postly.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.postly.features.onboarding.presentation.OnboardingScreen
import com.example.postly.features.splash.presentation.SplashScreen

@Composable
fun RootNavHost(sessionViewModel: SessionViewModel = hiltViewModel()) {
    val rootNavController = rememberNavController()
    val currentUser by sessionViewModel.currentUser.collectAsStateWithLifecycle()
    val pendingDeepLink by sessionViewModel.pendingDeepLink.collectAsStateWithLifecycle()

    fun forceLogoutToAuth() {
        sessionViewModel.clearSession()
        rootNavController.navigate(Route.AuthGraph) { popUpTo(0) { inclusive = true } }
    }

    LaunchedEffect(Unit) {
        sessionViewModel.sessionExpired.collect { forceLogoutToAuth() }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = Route.Splash
        ) {
            composable<Route.Splash> {
                SplashScreen(
                    onNavigateToOnboarding = {
                        rootNavController.navigate(Route.Onboarding) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        rootNavController.navigate(Route.MainGraph) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        rootNavController.navigate(Route.AuthGraph) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Route.Onboarding> {
                OnboardingScreen(
                    onFinished = {
                        rootNavController.navigate(Route.AuthGraph) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Route.AuthGraph> {
                AuthNavHost(
                    onAuthenticated = {
                        rootNavController.navigate(Route.MainGraph) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Route.MainGraph> {
                MainNavHost(
                    currentUser = currentUser,
                    onLoggedOut = { forceLogoutToAuth() },
                    pendingDeepLink = pendingDeepLink,
                    onDeepLinkConsumed = sessionViewModel::consumeDeepLink
                )
            }
        }
    }
}