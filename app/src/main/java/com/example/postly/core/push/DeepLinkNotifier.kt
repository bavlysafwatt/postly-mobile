package com.example.postly.core.push

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class PushDeepLink(
    val type: String,
    val postId: String?,
    val senderId: String?
)

@Singleton
class DeepLinkNotifier @Inject constructor() {
    private val _pendingDeepLink = MutableStateFlow<PushDeepLink?>(null)
    val pendingDeepLink: StateFlow<PushDeepLink?> = _pendingDeepLink.asStateFlow()

    fun notify(link: PushDeepLink) {
        _pendingDeepLink.value = link
    }

    fun consume() {
        _pendingDeepLink.value = null
    }
}