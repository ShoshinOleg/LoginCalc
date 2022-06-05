package com.shoshin.logincalc.ui.calculator

import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shoshin.logincalc.R
import com.shoshin.logincalc.core.BaseFragment
import com.shoshin.logincalc.core.validation.textWatcher
import com.shoshin.logincalc.databinding.ScreenCalculatorBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class CalculatorFragment: BaseFragment(R.layout.screen_calculator) {
    private val binding by viewBinding(ScreenCalculatorBinding::bind)
    private val viewModel: CalculatorViewModel by viewModels()

    override fun bind() {
        observe(viewModel.state) { state ->
            if (binding.number1.text?.toString() != state.number1.toString())
                binding.number1.setText(state.number1?.toString())
            if (binding.number2.text?.toString() != state.number2.toString())
                binding.number2.setText(state.number2?.toString())
            state.result?.let { result ->
                val format = DecimalFormat(
                    "###,###.#########",
                    DecimalFormatSymbols().apply { groupingSeparator = ' ' }
                )
                binding.result.text = format.format(result)
            }
            state.message?.let { message ->
                binding.result.text = message
            }
        }
    }

    override fun initViews(view: View) {
        with(binding) {
            root.setOnClickListener { hideKeyboard() }
            number1.addTextChangedListener(
                textWatcher {
                    it?.toString()?.toIntOrNull().let { number ->
                        viewModel.submitNumber1(number)
                    }
                }
            )
            number2.addTextChangedListener (
                textWatcher {
                    it?.toString()?.toIntOrNull().let { number ->
                        viewModel.submitNumber2(number)
                    }
                }
            )
            binding.multiply.setOnClickListener {
                viewModel.multiply()
                hideKeyboard()
            }
            binding.divide.setOnClickListener {
                viewModel.divide()
                hideKeyboard()
            }
            binding.plus.setOnClickListener {
                viewModel.plus()
                hideKeyboard()
            }
            binding.minus.setOnClickListener {
                viewModel.minus()
                hideKeyboard()
            }
        }
    }
}