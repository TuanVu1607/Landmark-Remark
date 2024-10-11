package com.technology.landmarkremark.viewmodel.address_note

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.technology.landmarkremark.common.swipe.SwipeActionDeleteEditListener
import com.technology.landmarkremark.common.swipe.SwipeGestureShowDeleteEdit
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.domain.repository.AddressNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressNoteViewModel @Inject constructor(private val addressNoteRepository: AddressNoteRepository) :
    ViewModel() {
    private val TAG = AddressNoteViewModel::class.java.simpleName

    private val searchKey: MutableLiveData<String> = MutableLiveData("")
    val addressNoteData: LiveData<List<AddressNoteEntity>> = searchKey.switchMap { text ->
        addressNoteRepository.searchAddressNote(text)
    }
    val editNoteData = MutableLiveData<Resource<Int>>()
    val deleteNoteData = MutableLiveData<Resource<Int>>()

    init {
        Log.i(TAG, "initViewModel()")
    }

    fun setTextSearch(text: String) = searchKey.postValue(text)

    fun applySwipeGestureForList(
        context: Context,
        recyclerView: RecyclerView,
        listener: SwipeActionDeleteEditListener
    ) {
        val swipeGestureShowDeleteEdit = object : SwipeGestureShowDeleteEdit(context, listener) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Xử lý sự kiện khi item bị vuốt
            }
        }
        swipeGestureShowDeleteEdit.handleActionButtonClick(recyclerView)
        val touchHelper = ItemTouchHelper(swipeGestureShowDeleteEdit)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    fun editAddressNote(addressNote: AddressNoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        editNoteData.postValue(Resource.Loading())
        editNoteData.postValue(handleEditNote(addressNoteRepository.updateAddressNote(addressNote)))
    }

    private fun handleEditNote(response: Int): Resource<Int> {
        if (response > 0) return Resource.Success(response)
        return Resource.Error("Chỉnh sửa note thất bại")
    }

    fun deleteAddressNote(addressNote: AddressNoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        deleteNoteData.postValue(Resource.Loading())
        deleteNoteData.postValue(
            handleDeleteNote(
                addressNoteRepository.deleteAddressNote(
                    addressNote
                )
            )
        )
    }

    private fun handleDeleteNote(response: Int): Resource<Int> {
        if (response > 0) return Resource.Success(response)
        return Resource.Error("Xóa note thất bại")
    }
}