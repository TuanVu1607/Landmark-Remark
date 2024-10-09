package com.technology.landmarkremark.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.technology.landmarkremark.databinding.ActivitySplashBinding
import com.technology.landmarkremark.ui.base.BaseActivity
import com.technology.landmarkremark.viewmodel.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        splashViewModel.goNextScreen(this)
    }
}