package com.shoshin.logincalc.core.storage

import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    fun isUserAuthorizedFlow(): Flow<Boolean>
    fun isUserAuthorized(): Boolean
    fun getAccessToken(): String?
    fun saveAccessToken(token: String?)
}