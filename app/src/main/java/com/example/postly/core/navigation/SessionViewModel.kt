package com.example.postly.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postly.core.data.local.CurrentUserCache
import com.example.postly.core.data.local.PushNotificationPreference
import com.example.postly.core.data.local.TokenManager
import com.example.postly.core.domain.model.User
import com.example.postly.core.network.SessionExpiredNotifier
import com.example.postly.core.push.DeepLinkNotifier
import com.example.postly.core.push.PushDeepLink
import com.example.postly.core.push.PushNotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val currentUserCache: CurrentUserCache,
    private val tokenManager: TokenManager,
    private val pushNotificationManager: PushNotificationManager,
    private val pushNotificationPreference: PushNotificationPreference,
    sessionExpiredNotifier: SessionExpiredNotifier,
    private val deepLinkNotifier: DeepLinkNotifier
) : ViewModel() {

    val currentUser: StateFlow<User?> = currentUserCache.observeUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val sessionExpired: SharedFlow<Unit> = sessionExpiredNotifier.events

    val pendingDeepLink: StateFlow<PushDeepLink?> = deepLinkNotifier.pendingDeepLink

    fun consumeDeepLink() = deepLinkNotifier.consume()

    fun clearSession() {
        viewModelScope.launch {
            val userId = currentUserCache.observeUser().first()?.id
            if (userId != null && pushNotificationPreference.isEnabled()) {
                runCatching { pushNotificationManager.unsubscribeForUser(userId) }
            }
            tokenManager.clearToken()
            currentUserCache.clear()
        }
    }
}