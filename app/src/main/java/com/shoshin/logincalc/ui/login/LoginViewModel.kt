package com.shoshin.logincalc.ui.login

import com.shoshin.logincalc.core.mvvm.BaseViewModel
import com.shoshin.logincalc.data.core.Resource
import com.shoshin.logincalc.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel() {

    private val mState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state = mState.asStateFlow()

    data class LoginState(
        val login: String? = "",
        val password: String? = "",
        val loading: Boolean = false,
        val errorMessage: String? = null
    )

    fun submitLogin(login: String) {
        if (mState.value.login != login)
            mState.value = mState.value.copy(login = login)
    }

    fun submitPassword(password: String) {
        if (mState.value.password != password)
            mState.value = mState.value.copy(password = password)
    }

    fun login() {
        val state = state.value
        launch {
            authRepository(
                AuthRepository.Params(state.login, state.password)
            ).collect() {
                when (it) {
                    is Resource.Loading -> {
                        mState.value = mState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Error -> {
                        mState.value = mState.value.copy(
                            loading = false,
                            errorMessage = it.failure.message()
                        )
                    }
                    is Resource.Success -> {}
                }
            }
        }
    }
}