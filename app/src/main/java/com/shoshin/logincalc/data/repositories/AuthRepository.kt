package com.shoshin.logincalc.data.repositories

import com.google.gson.Gson
import com.shoshin.logincalc.core.storage.AuthStorage
import com.shoshin.logincalc.data.LoginData
import com.shoshin.logincalc.data.core.BaseResponse
import com.shoshin.logincalc.data.core.Error
import com.shoshin.logincalc.data.core.Repository
import com.shoshin.logincalc.data.core.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authStorage: AuthStorage
): Repository<LoginData, AuthRepository.Params>() {
    override suspend fun run(params: Params): Flow<Resource<com.shoshin.logincalc.data.LoginData>> =
        execute {
            delay(2000L)
            if (params.login == "admin" && params.password == "123")
                return@execute LoginData("admin123")
            else {
                val json = Gson().toJson(
                    BaseResponse(
                        null,
                        Error(
                            "Неверный логин или пароль",
                            401
                        ),
                        401
                    )
                )
                throw HttpException(
                    Response.error<BaseResponse<LoginData>>(
                        401,
                        ResponseBody.create(
                            "application/json".toMediaTypeOrNull(),
                            json.toByteArray())
                    )
                )
            }
        }


    override val doOnSuccess: suspend (LoginData?, Params) -> Unit
        get() = { data, _ ->
            data?.let {
                authStorage.saveAccessToken(it.accessToken)
            }
        }

    class Params(val login: String?, val password: String?)
}