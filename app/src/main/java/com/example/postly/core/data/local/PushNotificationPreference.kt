package com.example.postly.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val ENABLED = booleanPreferencesKey("push_notifications_enabled")
    }

    suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.ENABLED] = enabled }
    }

    fun observeEnabled(): Flow<Boolean> = dataStore.data.map { it[Keys.ENABLED] ?: false }

    suspend fun isEnabled(): Boolean = dataStore.data.map { it[Keys.ENABLED] ?: false }.first()
}