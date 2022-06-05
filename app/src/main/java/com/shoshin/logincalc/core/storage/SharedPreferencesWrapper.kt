package com.shoshin.logincalc.core.storage

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SharedPreferencesWrapper @Inject constructor(
    private val preferences: SharedPreferences
): AppStorage {
    override fun putString(key: String, value: String?) {
        preferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String? = preferences.getString(key, null)

    override fun <T> asFlow(
        keyPredicate: (key: String) -> Boolean,
        transform: (appStorage: AppStorage) -> T
    ): Flow<T> {
        val flow = MutableStateFlow(transform(this))
        val onSharedPreferencesChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (keyPredicate(key)) {
                    flow.value = transform(this)
                }
            }
        preferences.registerOnSharedPreferenceChangeListener(
            onSharedPreferencesChangeListener
        )
        return flow.onCompletion {
            preferences.unregisterOnSharedPreferenceChangeListener(
                onSharedPreferencesChangeListener
            )
        }
    }
}