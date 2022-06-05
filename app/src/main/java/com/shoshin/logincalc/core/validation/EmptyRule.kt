package com.shoshin.logincalc.core.validation

import android.content.Context
import androidx.annotation.StringRes
import com.shoshin.logincalc.R

class EmptyRule(
    private val context: Context?,
    @StringRes private val errorStringRes: Int = R.string.error_required_field
) : ValidationRule {
    override fun isValid(value: String?) =
        value?.isNotEmpty() == true

    override fun error(): String? = context?.getString(errorStringRes)
}
