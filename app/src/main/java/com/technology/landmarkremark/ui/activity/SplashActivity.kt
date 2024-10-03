package com.technology.landmarkremark.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.technology.landmarkremark.databinding.ActivitySplashBinding
import com.technology.landmarkremark.preferences.PreferenceManager
import com.technology.landmarkremark.ui.base.BaseActivity
import com.technology.landmarkremark.viewmodel.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val TAG = SplashActivity::class.java.simpleName

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !splashViewModel.isReady.value
            }
        }

        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000L)
            if (splashViewModel.isReady.value) {
                goToHome()
            } else {
                goToLogin()
            }
        }
    }

    private fun goToHome() {
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)
        }
    }

    private fun goToLogin() {
        Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(this)
        }
    }
}