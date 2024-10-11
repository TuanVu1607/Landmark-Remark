package com.technology.landmarkremark.data.repository

import androidx.lifecycle.LiveData
import com.technology.landmarkremark.data.dao.AddressNoteDao
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.domain.repository.AddressNoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressNoteRepositoryImp @Inject constructor(private val addressNoteDao: AddressNoteDao) :
    AddressNoteRepository {
    override fun getUserAddressHistory(userId: Int): LiveData<List<AddressNoteEntity>> =
        addressNoteDao.getUserAddressNoteHistory(userId)

    override suspend fun updateAddressNote(addressNote: AddressNoteEntity): Int =
        addressNoteDao.update(addressNote)

    override suspend fun deleteAddressNote(addressNote: AddressNoteEntity): Int =
        addressNoteDao.delete(addressNote)

    override fun searchAddressNote(searchQuery: String): LiveData<List<AddressNoteEntity>> =
        addressNoteDao.searchAddressNote(searchQuery)
}