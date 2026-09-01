package com.example.postly.features.auth.presentation.register

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, nameError = null) }
    fun onUsernameChange(value: String) =
        _uiState.update { it.copy(username = value, usernameError = null) }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, emailError = null) }
    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }

    fun onPhotoSelected(uri: Uri?) = _uiState.update { it.copy(photoUri = uri) }

    fun register() {
        val state = _uiState.value

        val nameError =
            if (state.name.trim().length < 3) "Name must be at least 3 characters" else null
        val usernameError = when {
            state.username.isBlank() -> "Username is required"
            state.username.length < 3 -> "Username must be at least 3 characters"
            !state.username.all { it.isLetterOrDigit() } -> "Letters and numbers only"
            else -> null
        }
        val emailError = if (!Patterns.EMAIL_ADDRESS.matcher(state.email)
                .matches()
        ) "Enter a valid email" else null
        val passwordError =
            if (state.password.length < 6) "Password must be at least 6 characters" else null
        val confirmPasswordError =
            if (state.confirmPassword != state.password) "Passwords don't match" else null

        if (listOf(
                nameError,
                usernameError,
                emailError,
                passwordError,
                confirmPasswordError
            ).any { it != null }
        ) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    usernameError = usernameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = registerUseCase(
                name = state.name.trim(),
                username = state.username.trim(),
                email = state.email.trim(),
                password = state.password,
                confirmPassword = state.confirmPassword,
                photoUri = state.photoUri
            )
            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        registerSuccess = true
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.toUserMessage()
                    )
                }
            }
        }
    }
}