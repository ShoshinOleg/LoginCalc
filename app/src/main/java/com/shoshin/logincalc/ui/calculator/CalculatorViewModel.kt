package com.shoshin.logincalc.ui.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel: ViewModel() {

    private val mState: MutableStateFlow<CalcState> = MutableStateFlow(CalcState())
    val state = mState.asStateFlow()

    data class CalcState(
        val number1: Int? = null,
        val number2: Int? = null,
        val result: Double? = null,
        val message: String? = null
    )

    fun submitNumber1(number: Int?) {
        Log.e("n1","n1=$number")
        if (mState.value.number1 != number)
            mState.value = mState.value.copy(
                number1 = number
            )
    }

    fun submitNumber2(number: Int?) {
        if (mState.value.number2 != number)
            mState.value = mState.value.copy(
                number2 = number
            )
    }

    fun multiply() {
        val n1 = mState.value.number1 ?: 0
        val n2 = mState.value.number2?.toDouble() ?: 0.0
        mState.value = mState.value.copy(
            result = n1 * n2,
            message = null
        )
    }

    fun divide() {
        val n1 = mState.value.number1 ?: 0
        val n2 = mState.value.number2?.toDouble() ?: 0.0
        if (mState.value.number2 == 0)
            mState.value = mState.value.copy(
                message = "Разделить на ноль нельзя"
            )
        else
            mState.value = mState.value.copy(
                result = n1 / n2,
                message = null
            )
    }

    fun plus() {
        val n1 = mState.value.number1 ?: 0
        val n2 = mState.value.number2?.toDouble() ?: 0.0
        mState.value = mState.value.copy(
            result = n1 + n2,
            message = null
        )
    }

    fun minus() {
        val n1 = mState.value.number1 ?: 0
        val n2 = mState.value.number2?.toDouble() ?: 0.0
        mState.value = mState.value.copy(
            result = n1 - n2,
            message = null
        )
    }
}