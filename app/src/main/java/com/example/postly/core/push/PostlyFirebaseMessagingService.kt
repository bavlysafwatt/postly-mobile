package com.example.postly.core.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.postly.MainActivity
import com.example.postly.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class PostlyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title = data["title"] ?: return
        val body = data["body"] ?: ""

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_TYPE, data["type"])
            putExtra(EXTRA_POST_ID, data["postId"])
            putExtra(EXTRA_SENDER_ID, data["senderId"])
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        ContextCompat.getSystemService(this, NotificationManager::class.java)
            ?.notify(Random.nextInt(), notification)
    }

    @Deprecated("Deprecated in Java")
    override fun onNewToken(token: String) {
        // Handle the new token if needed
    }

    companion object {
        const val CHANNEL_ID = "postly_activity"
        const val EXTRA_TYPE = "push_type"
        const val EXTRA_POST_ID = "push_post_id"
        const val EXTRA_SENDER_ID = "push_sender_id"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Activity",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Likes, comments, and new followers"
            }
            ContextCompat.getSystemService(context, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
}