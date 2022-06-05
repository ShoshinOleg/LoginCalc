package com.shoshin.logincalc.core.validation

import android.text.Editable
import android.text.TextWatcher

fun textWatcher(
    afterTextChanged: (text: Editable?) ->  Unit
): TextWatcher =
    object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(text: Editable?) {
            afterTextChanged.invoke(text)
        }
    }