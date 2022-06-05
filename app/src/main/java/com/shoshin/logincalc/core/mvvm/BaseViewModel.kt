package com.shoshin.logincalc.core.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    protected fun launch(func: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { func() }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}