package com.example.postly.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.postly.features.auth.presentation.forgotpassword.ForgotPasswordScreen
import com.example.postly.features.auth.presentation.login.LoginScreen
import com.example.postly.features.auth.presentation.register.RegisterScreen
import com.example.postly.features.auth.presentation.resetpassword.ResetPasswordScreen

@Composable
fun AuthNavHost(onAuthenticated: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Auth.Login
    ) {
        composable<Route.Auth.Login> {
            LoginScreen(
                onLoginSuccess = onAuthenticated,
                onNavigateToRegister = { navController.navigate(Route.Auth.Register) },
                onNavigateToForgotPassword = { navController.navigate(Route.Auth.ForgotPassword) }
            )
        }
        composable<Route.Auth.Register> {
            RegisterScreen(
                onRegisterSuccess = onAuthenticated,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable<Route.Auth.ForgotPassword> {
            ForgotPasswordScreen(
                onOtpSent = { email -> navController.navigate(Route.Auth.ResetPassword(email)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Route.Auth.ResetPassword> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.Auth.ResetPassword>()
            ResetPasswordScreen(
                email = args.email,
                onResetSuccess = onAuthenticated,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}