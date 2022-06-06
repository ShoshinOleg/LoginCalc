package com.shoshin.logincalc.ui.login

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shoshin.logincalc.R
import com.shoshin.logincalc.core.BaseFragment
import com.shoshin.logincalc.core.validation.EmptyRule
import com.shoshin.logincalc.core.validation.ValidatorsComposer
import com.shoshin.logincalc.core.validation.textWatcher
import com.shoshin.logincalc.databinding.ScreenLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    private var loginValidator: ValidatorsComposer? = null
    private var passwordValidator: ValidatorsComposer? = null

    private val loginTextWatcher: TextWatcher = textWatcher {
        it?.toString()?.let { login ->
            viewModel.submitLogin(login)
        }
    }

    private val passwordTextWatcher: TextWatcher = textWatcher {
        it?.toString()?.let { password ->
            viewModel.submitPassword(password)
        }
    }

    override fun bind() {
        observe(viewModel.state) { state ->
            with(binding) {
                if (login.text?.toString() != state.login) {
                    login.removeTextChangedListener(loginTextWatcher)
                    login.setText(state.login)
                    login.addTextChangedListener(loginTextWatcher)
                }
                if (password.text?.toString() != state.password) {
                    password.removeTextChangedListener(passwordTextWatcher)
                    password.setText(state.password)
                    password.addTextChangedListener(passwordTextWatcher)
                }
                if (state.loading) {
                    mainConstraint.isVisible = false
                    progressLayout.isVisible = true
                } else {
                    mainConstraint.isVisible = true
                    progressLayout.isVisible = false
                }
                if (state.errorMessage != null) {
                    errorLabel.isVisible = true
                    errorLabel.text = state.errorMessage
                } else {
                    errorLabel.isVisible = false
                }
            }
        }
    }

    override fun initViews(view: View) {
        initValidators()
        with(binding) {
            root.setOnClickListener { hideKeyboard() }
            login.addTextChangedListener(loginTextWatcher)
            password.addTextChangedListener(passwordTextWatcher)
            nextButton.setOnClickListener {
                if (!ValidatorsComposer.isValid(
                        loginValidator,
                        passwordValidator
                    )
                ) return@setOnClickListener
                viewModel.login()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.addTextChangedListener {
            viewModel.submitLogin(it.toString())
        }
        binding.password.addTextChangedListener {
            viewModel.submitPassword(it.toString())
        }
        binding.nextButton.setOnClickListener {
            if (!ValidatorsComposer.isValid(
                    loginValidator,
                    passwordValidator
                )
            ) return@setOnClickListener
            hideKeyboard()
            viewModel.login()
        }
    }

    private fun initValidators() {
        loginValidator = ValidatorsComposer(
            rules = arrayOf(
                EmptyRule(requireContext())
            ),
            binding.login,
            binding.loginLayout,
            lifecycle
        )
        passwordValidator = ValidatorsComposer(
            rules = arrayOf(
                EmptyRule(requireContext())
            ),
            binding.password,
            binding.passwordLayout,
            lifecycle
        )
    }
}