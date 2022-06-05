package com.shoshin.logincalc.core.storage

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthStorageImpl @Inject constructor(
    private val appStorage: AppStorage
): AuthStorage {

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    override fun isUserAuthorizedFlow(): Flow<Boolean> =
        appStorage.asFlow(
            { key -> key == ACCESS_TOKEN },
            { isUserAuthorized() }
        )

    override fun isUserAuthorized(): Boolean = !appStorage.getString(ACCESS_TOKEN).isNullOrBlank()

    override fun getAccessToken(): String? = appStorage.getString(ACCESS_TOKEN)

    override fun saveAccessToken(token: String?) {
        appStorage.putString(ACCESS_TOKEN, token)
    }
}