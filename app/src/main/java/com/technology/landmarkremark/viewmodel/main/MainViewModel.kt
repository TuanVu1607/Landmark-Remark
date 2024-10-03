package com.technology.landmarkremark.viewmodel.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.data.model.AddressLocation
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.domain.repository.MainRepository
import com.technology.landmarkremark.ui.activity.LoginActivity
import com.technology.landmarkremark.ui.activity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

    var currentLocation: AddressLocation? = null
    var userData: UserEntity? = null
    val createNoteData = MutableLiveData<Resource<Long>>()

    init {
        Log.i(TAG, "initViewModel()")
    }

    fun getUser() = viewModelScope.launch(Dispatchers.IO) {
        userData = mainRepository.getUserData()
    }

    fun createNote(userAddressNote: UserAddressNote) = viewModelScope.launch(Dispatchers.IO) {
        createNoteData.postValue(Resource.Loading())
        createNoteData.postValue(handleCreateNote(mainRepository.createNote(userAddressNote)))
    }

    private fun handleCreateNote(response: Long): Resource<Long> {
        if (response > 0) return Resource.Success(response)
        return Resource.Error("Tạo note thất bại")
    }

    fun logout(activity: Activity) = viewModelScope.launch(Dispatchers.IO) {
        userData?.let {
            mainRepository.deleteUserData(it.id)
        }
        Intent(activity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(this)
        }
    }
}