package com.shoshin.logincalc.core.storage

import kotlinx.coroutines.flow.Flow

interface AppStorage {
    fun putString(key: String, value: String?)
    fun getString(key: String): String?

    fun <T> asFlow(
        keyPredicate: (key: String) -> Boolean,
        transform: (appStorage: AppStorage) -> T
    ): Flow<T>
}
