package com.shoshin.logincalc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.shoshin.logincalc.core.storage.AuthStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    @Inject
    lateinit var router: Router
    @Inject
    lateinit var authStorage: AuthStorage

    private val navigator: Navigator by lazy { AppNavigator(this, R.id.container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LoginCalc)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe(authStorage.isUserAuthorizedFlow(), ::onAuthFlow)
    }


    private fun onAuthFlow(isAuth: Boolean) {
        if (isAuth)
            router.newRootScreen(Screens.calculator())
        else
            router.newRootScreen(Screens.login())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun <T : Any, F : Flow<T>> observe(flow: F, body: (T) -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect { body(it) }
            }
        }
    }
}