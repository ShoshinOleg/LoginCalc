package com.shoshin.logincalc.data.core

data class BaseResponse<T> (
    var data: T? = null,
    var error: Error? = null,
    var code: Int = 0
)

