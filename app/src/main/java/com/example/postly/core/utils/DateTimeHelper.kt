package com.example.postly.core.utils

import java.time.Duration
import java.time.Instant

object DateTimeHelper {

    fun getRelativeTime(dateString: String): String {
        return try {
            val createdAt = Instant.parse(dateString)
            val now = Instant.now()

            val duration = Duration.between(createdAt, now)

            when {
                duration.toMinutes() < 1 -> "Just now"
                duration.toHours() < 1 -> "${duration.toMinutes()}m ago"
                duration.toDays() < 1 -> "${duration.toHours()}h ago"
                duration.toDays() < 7 -> "${duration.toDays()}d ago"
                else -> "${duration.toDays() / 7}w ago"
            }
        } catch (e: Exception) {
            ""
        }
    }
}