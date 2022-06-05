package com.shoshin.logincalc.core

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment(
    @LayoutRes layoutRes: Int
): Fragment(layoutRes), BackButtonListener, RouterProvider {

    override val router: Router by lazy { ((parentFragment ?: activity) as RouterProvider).router }

    abstract fun bind()
    abstract fun initViews(view: View)

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "onStart")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("onViewStateRestored", "onViewStateRestored")
        initViews(requireView())
        bind()
    }

    fun <T : Any, F : Flow<T>> observe(flow: F, body: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect { body(it) }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun hideKeyboard(view: View? = activity?.currentFocus) {
        view?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}