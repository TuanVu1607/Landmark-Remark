package com.technology.landmarkremark.viewmodel.splash

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technology.landmarkremark.domain.repository.SplashRepository
import com.technology.landmarkremark.ui.activity.LoginActivity
import com.technology.landmarkremark.ui.activity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val splashRepository: SplashRepository) :
    ViewModel() {
    private val TAG = SplashViewModel::class.java.simpleName

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        Log.i(TAG, "initViewModel()")
        checkLogin()
    }

    private fun checkLogin() = viewModelScope.launch {
        delay(2000L)
        _isReady.value = splashRepository.checkLogin()
    }

    fun goNextScreen(activity: Activity) = viewModelScope.launch {
        delay(2000L)
        if (_isReady.value) {
            goToHome(activity)
        } else {
            goToLogin(activity)
        }
    }

    private fun goToHome(activity: Activity) {
        Intent(activity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(this)
        }
    }

    private fun goToLogin(activity: Activity) {
        Intent(activity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(this)
        }
    }
}