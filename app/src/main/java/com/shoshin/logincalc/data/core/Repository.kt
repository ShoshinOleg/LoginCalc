package com.shoshin.logincalc.data.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class Repository<Type, in Params> {
    abstract suspend fun run(params: Params): Flow<Resource<Type>>

    open val doOnSuccess: suspend (Type?, Params) -> Unit = { _, _ -> }
    open val doOnCompletion: () -> Unit = {}

    open suspend operator fun invoke(
        params: Params,
        doOnCancel: suspend (Resource.Error<Type>) -> Unit = {}
    ): Flow<Resource<Type>> {
        return run(params).onStart {
            emit(Resource.Loading())
        }.onEach {
            if (it is Resource.Success) {
                doOnSuccess(it.data, params)
            }
        }.onCompletion {
            if (it is CancellationException) {
                doOnCompletion()
                doOnCancel(Resource.Error(Failure.Canceled))
            }
        }
    }

    protected suspend fun <T> execute(
        invoke: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(
            try {
                val response = invoke()
                Resource.Success(response)
            } catch (ex: Exception) {
                Failure.handle(ex).let {
                    Resource.Error(it)
                }
            }
        )
    }.flowOn(Dispatchers.IO)
}