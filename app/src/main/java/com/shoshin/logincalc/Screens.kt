package com.shoshin.logincalc

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.shoshin.logincalc.ui.calculator.CalculatorFragment
import com.shoshin.logincalc.ui.login.LoginFragment

object Screens {
    fun login() = FragmentScreen { LoginFragment() }
    fun calculator() = FragmentScreen { CalculatorFragment() }
}