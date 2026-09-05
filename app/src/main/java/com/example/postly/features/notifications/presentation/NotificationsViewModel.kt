package com.example.postly.features.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.postly.core.domain.Result
import com.example.postly.core.network.toUserMessage
import com.example.postly.features.notifications.domain.model.Notification
import com.example.postly.features.notifications.domain.model.NotificationType
import com.example.postly.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.postly.features.notifications.domain.usecase.MarkAllAsReadUseCase
import com.example.postly.features.notifications.domain.usecase.MarkAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NotificationNavigationEvent {
    data class ToPostDetail(val postId: String) : NotificationNavigationEvent
    data class ToUserProfile(val userId: String) : NotificationNavigationEvent
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val markAllAsReadUseCase: MarkAllAsReadUseCase
) : ViewModel() {

    val notifications: Flow<PagingData<Notification>> =
        getNotificationsUseCase().cachedIn(viewModelScope)

    private val _readOverlay = MutableStateFlow<Set<String>>(emptySet())
    val readOverlay: StateFlow<Set<String>> = _readOverlay.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NotificationNavigationEvent>()
    val navigationEvent: SharedFlow<NotificationNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _refreshEvent = MutableSharedFlow<Unit>()
    val refreshEvent: SharedFlow<Unit> = _refreshEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    fun onNotificationClick(notification: Notification) {
        markAsReadOptimistically(notification)

        val event = when (notification.type) {
            NotificationType.LIKE, NotificationType.COMMENT ->
                notification.postId?.let { NotificationNavigationEvent.ToPostDetail(it) }

            NotificationType.FOLLOW ->
                notification.sender?.id?.let { NotificationNavigationEvent.ToUserProfile(it) }

            NotificationType.UNKNOWN -> null
        }

        event?.let { viewModelScope.launch { _navigationEvent.emit(it) } }
    }

    private fun markAsReadOptimistically(notification: Notification) {
        if (notification.isRead || _readOverlay.value.contains(notification.id)) return
        _readOverlay.update { it + notification.id }
        viewModelScope.launch { markAsReadUseCase(notification.id) }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            when (val result = markAllAsReadUseCase()) {
                is Result.Success -> {
                    _readOverlay.value = emptySet()
                    _refreshEvent.emit(Unit)
                }

                is Result.Failure -> _errorEvent.emit(result.error.toUserMessage())
            }
        }
    }
}