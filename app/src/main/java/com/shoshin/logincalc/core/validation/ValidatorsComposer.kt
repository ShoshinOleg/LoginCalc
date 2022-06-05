package com.shoshin.logincalc.core.validation

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import com.google.android.material.textfield.TextInputLayout

class ValidatorsComposer(
    private vararg val rules: ValidationRule,
    private val editText: EditText,
    private val label: TextInputLayout,
    lifecycle: Lifecycle,
    strategy: ValidationStrategy = ValidationStrategy.Debounce(1_000L)
) {
    private val listener: TextWatcher
    private var handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            validate()
        }
    }

    init {
        listener = object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                when (strategy) {
                    is ValidationStrategy.Immediately -> {
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            validate()
                        }
                    }
                    is ValidationStrategy.Debounce -> {
                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, 1_000L)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        editText.addTextChangedListener(listener)
    }

    companion object {
        fun isValid(vararg validators: ValidatorsComposer?): Boolean =
            validators.filterNotNull().map { it.validate() }.none { !it }
    }

    fun validate(): Boolean {
        for (validator in rules) {
            if (!validator.isValid(editText.text.toString())) {
                label.error = validator.error()
                return false
            }
        }
        label.error = null
        return true
    }

    fun disable() {
        handler.removeCallbacks(runnable)
        editText.removeTextChangedListener(listener)
    }
}

sealed class ValidationStrategy {
    object Immediately : ValidationStrategy()
    class Debounce(timeout: Long = 1_000) : ValidationStrategy()
}