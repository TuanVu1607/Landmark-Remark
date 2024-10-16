package com.technology.landmarkremark.domain.repository

import androidx.lifecycle.LiveData
import com.technology.landmarkremark.data.entity.AddressNoteEntity

interface AddressNoteRepository {
    fun getUserAddressHistory(userId: Int): LiveData<List<AddressNoteEntity>>

    suspend fun updateAddressNote(addressNote: AddressNoteEntity): Int

    suspend fun deleteAddressNote(addressNote: AddressNoteEntity): Int

    fun searchAddressNote(searchQuery: String): LiveData<List<AddressNoteEntity>>
}