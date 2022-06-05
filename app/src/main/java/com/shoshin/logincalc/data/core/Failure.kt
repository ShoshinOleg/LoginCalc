package com.shoshin.logincalc.data.core

import com.google.gson.Gson
import retrofit2.HttpException
import java.net.HttpURLConnection

sealed class Failure {
    data class ServerError(private val error: String) : Failure() {
        override fun message() = error
    }

    object Canceled : Failure() {
        override fun message(): String = ""
    }

    data class Unauthorized(private val message: String): Failure() {
        override fun message(): String = message
    }

    abstract fun message(): String

    companion object {
        fun default() = ServerError("Неизвестная ошибка")
        fun handle(exception: Exception): Failure {
            val errorResponse: BaseResponse<*>? = when (exception) {
                is HttpException -> {
                    val errorResponse: BaseResponse<*>? = try {
                        val errorBody = exception.response()?.errorBody()?.string()
                        val gson = Gson()
                        gson.fromJson(errorBody, BaseResponse::class.java)
                    } catch (ex: Exception) {
                        null
                    }
                    errorResponse?.code = exception.code()
                    errorResponse
                }
                else -> null
            }
            return when (errorResponse?.code) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> Unauthorized(
                    errorResponse.error?.message ?: "Неверный логин или пароль"
                )
                else -> ServerError(errorResponse?.error?.message ?: "Неизвестная ошибка.")
            }
        }
    }
}