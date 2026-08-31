package com.example.postly.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    suspend fun setCompleted() {
        dataStore.edit { prefs -> prefs[Keys.COMPLETED] = true }
    }

    suspend fun hasCompletedOnboarding(): Boolean =
        dataStore.data.map { it[Keys.COMPLETED] ?: false }.first()
}