package com.technology.landmarkremark.viewmodel.address_note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.domain.repository.AddressNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressNoteViewModel @Inject constructor(private val addressNoteRepository: AddressNoteRepository) :
    ViewModel() {
    private val TAG = AddressNoteViewModel::class.java.simpleName

    private val searchKey: MutableLiveData<String> = MutableLiveData("")
    val addressNoteData: LiveData<List<AddressNoteEntity>> = searchKey.switchMap { text ->
        addressNoteRepository.searchAddressNote(text)
    }

    init {
        Log.i(TAG, "initViewModel()")
    }

    fun setTextSearch(text: String) = searchKey.postValue(text)
}