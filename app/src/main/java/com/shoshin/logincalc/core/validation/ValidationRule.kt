package com.shoshin.logincalc.core.validation

interface ValidationRule {
    fun isValid(value: String?): Boolean
    fun error(): String?
}