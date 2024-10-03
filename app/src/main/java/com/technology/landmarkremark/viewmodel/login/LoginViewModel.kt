package com.technology.landmarkremark.viewmodel.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technology.landmarkremark.common.extensions.isValidName
import com.technology.landmarkremark.common.extensions.toast
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.domain.repository.LoginRepository
import com.technology.landmarkremark.ui.activity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName

    val username: ObservableField<String> = ObservableField("")
    val usernameError: ObservableField<String> = ObservableField("")
    val isEnableLoginButton: ObservableField<Boolean> = ObservableField(false)
    val isShowClear: ObservableField<Boolean> = ObservableField(false)
    val isShowErrorUsername: ObservableField<Boolean> = ObservableField(false)
    val loginData = MutableLiveData<Resource<UserEntity>>()

    init {
        Log.i(TAG, "initViewModel()")
    }

    fun clearUsername() {
        username.set("")
    }

    fun eventChangeTextUsername() {
        isShowClear.set(username.get()!!.isNotEmpty())
        validateUsername()
        enableButton()
    }

    private fun validateUsername() {
        usernameError.set(
            if (username.get()!!.isValidName()) "Tên đăng nhập phải từ 2 kí tự trở lên" else ""
        )
        isShowErrorUsername.set(!username.get()!!.isValidName())
    }

    private fun enableButton() {
        isEnableLoginButton.set(username.get()!!.isValidName())
    }

    fun onLogin() = viewModelScope.launch(Dispatchers.IO) {
        loginData.postValue(Resource.Loading())
        loginData.postValue(handleLoginResponse(loginRepository.loginUser(username.get()!!)))
    }

    private fun handleLoginResponse(loginResponse: UserEntity?): Resource<UserEntity> {
        if (loginResponse == null) return Resource.Error("User này chưa đăng ký")
        return Resource.Success(loginResponse)
    }

    fun saveUserAndGoToHome(activity: Activity) = viewModelScope.launch(Dispatchers.IO) {
        loginData.value?.data?.let { user ->
            loginRepository.saveUser(user)
            Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activity.startActivity(this)
            }
        }
    }

    fun registerUserAndGoToHome(activity: Activity) = viewModelScope.launch(Dispatchers.IO) {
        val user = UserEntity()
        user.name = username.get()!!
        if (loginRepository.registerUser(user) >= 1) {
            onLogin()
        } else {
            activity.toast("Đăng ký thất bại")
        }
    }
}