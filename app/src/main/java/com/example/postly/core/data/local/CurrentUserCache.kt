package com.example.postly.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.postly.core.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserCache @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {

    private object Keys {
        val CURRENT_USER = stringPreferencesKey("current_user")
    }

    suspend fun save(user: User) {
        dataStore.edit { prefs -> prefs[Keys.CURRENT_USER] = gson.toJson(user) }
    }

    suspend fun clear() {
        dataStore.edit { prefs -> prefs.remove(Keys.CURRENT_USER) }
    }

    fun observeUser(): Flow<User?> = dataStore.data.map { prefs ->
        prefs[Keys.CURRENT_USER]?.let { json ->
            runCatching { gson.fromJson(json, User::class.java) }.getOrNull()
        }
    }
}