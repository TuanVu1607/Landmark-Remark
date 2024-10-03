package com.technology.landmarkremark.viewmodel.address_note

import android.util.Log
import androidx.lifecycle.ViewModel
import com.technology.landmarkremark.domain.repository.AddressNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressNoteViewModel @Inject constructor(private val addressNoteRepository: AddressNoteRepository) :
    ViewModel() {
    private val TAG = AddressNoteViewModel::class.java.simpleName

    init {
        Log.i(TAG, "initViewModel()")
    }
}