package com.example.postly.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.model.User
import com.example.postly.core.network.SessionExpiredNotifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val currentUserCache: CurrentUserCache,
    private val tokenManager: TokenManager,
    sessionExpiredNotifier: SessionExpiredNotifier
) : ViewModel() {

    val currentUser: StateFlow<User?> = currentUserCache.observeUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val sessionExpired: SharedFlow<Unit> = sessionExpiredNotifier.events

    fun clearSession() {
        viewModelScope.launch {
            tokenManager.clearToken()
            currentUserCache.clear()
        }
    }
}