package com.shoshin.logincalc.data.core

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(val failure: Failure, data: T? = null) : Resource<T>(data, failure.message())
}