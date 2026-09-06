package com.example.postly.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Onboarding : Route

    // Root-level graph containers
    @Serializable
    data object AuthGraph : Route
    @Serializable
    data object MainGraph : Route

    sealed interface Auth : Route {
        @Serializable data object Login : Auth
        @Serializable data object Register : Auth
        @Serializable data object ForgotPassword : Auth
        @Serializable data class ResetPassword(val email: String) : Auth
    }

    // bottom-nav tabs
    @Serializable data object Home : Route
    @Serializable data object Search : Route
    @Serializable data object Notifications : Route
    @Serializable
    data object Profile : Route

    // stacked screens, no bottom bar, reachable from any tab
    @Serializable data class UserProfile(val userId: String) : Route
    @Serializable data class PostDetail(val postId: String) : Route
    @Serializable data class CreateOrEditPost(val postId: String? = null) : Route
    @Serializable data class Followers(val userId: String) : Route
    @Serializable data class Following(val userId: String) : Route
    @Serializable data object EditProfile : Route
    @Serializable data object Settings : Route
    @Serializable
    data object ChangePassword : Route
}