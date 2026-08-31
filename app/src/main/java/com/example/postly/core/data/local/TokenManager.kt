package com.example.postly.core.data.local

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("access_token")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[Keys.TOKEN] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(Keys.TOKEN) }
    }

    fun observeToken(): Flow<String?> = dataStore.data.map { it[Keys.TOKEN] }

    suspend fun getTokenSnapshot(): String? = dataStore.data.map { it[Keys.TOKEN] }.first()

    suspend fun isTokenPresentAndNotExpired(): Boolean {
        val token = getTokenSnapshot() ?: return false
        return try {
            val payload = token.split(".").getOrNull(1) ?: return false
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val json = String(decoded, Charsets.UTF_8)
            @Suppress("UNCHECKED_CAST")
            val claims = gson.fromJson(json, Map::class.java) as Map<String, Any?>
            val exp = claims["exp"] as? Double
            exp != null && exp > (System.currentTimeMillis() / 1000.0)
        } catch (e: Exception) {
            false
        }
    }
}