package com.technology.landmarkremark.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.technology.landmarkremark.constants.DATABASE_ERROR
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.databinding.ActivityLoginBinding
import com.technology.landmarkremark.interfaces.RegisterDialogListener
import com.technology.landmarkremark.ui.base.BaseActivity
import com.technology.landmarkremark.ui.dialog.ProgressDialog
import com.technology.landmarkremark.ui.dialog.RegisterDialog
import com.technology.landmarkremark.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),
    RegisterDialogListener {
    private val TAG = LoginActivity::class.java.simpleName

    private val loginViewModel by viewModels<LoginViewModel>()

    private var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")
        binding.loginViewModel = loginViewModel
        loadingDialog = ProgressDialog(this)
        observeLoginData()
    }

    private fun observeLoginData() {
        binding.loginViewModel?.loginData?.observe(this) { result ->
            when (result) {
                is Resource.Loading -> loadingDialog?.createProgressDialog()

                is Resource.Error -> {
                    loadingDialog?.cancelDialog(true)
                    result.message?.let { errorMess ->Log.e(DATABASE_ERROR, errorMess) }
                    RegisterDialog(this).createDialog(this)
                }

                is Resource.Success -> {
                    loadingDialog?.cancelDialog(true)
                    result.data?.let { loginViewModel.saveUserAndGoToHome(this) }
                }
            }
        }
    }

    override fun onAction(isAgree: Boolean) {
        if (isAgree)
            loginViewModel.registerUserAndGoToHome(this)
    }
}