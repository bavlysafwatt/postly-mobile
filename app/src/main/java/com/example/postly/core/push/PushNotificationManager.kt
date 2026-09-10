package com.example.postly.core.push

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationManager @Inject constructor() {

    suspend fun subscribeForUser(userId: String) {
        Firebase.messaging.subscribeToTopic(topicFor(userId)).await()
        Log.d("PushNotificationManager", "Subscribed to push notifications for user: $userId")
    }

    suspend fun unsubscribeForUser(userId: String) {
        Firebase.messaging.unsubscribeFromTopic(topicFor(userId)).await()
        Log.d("PushNotificationManager", "Unsubscribed from push notifications for user: $userId")
    }

    private fun topicFor(userId: String) = "user_$userId"
}