package com.technology.landmarkremark.data.repository

import com.technology.landmarkremark.data.dao.AddressNoteDao
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.domain.repository.MainRepository
import com.technology.landmarkremark.common.preferences.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImp @Inject constructor(
    private val preferences: Preferences,
    private val addressNoteDao: AddressNoteDao,
) : MainRepository {
    override suspend fun getUserData() = preferences.getUserData()

    override suspend fun createNote(userAddressNote: UserAddressNote): Long {
        return addressNoteDao.insert(
            AddressNoteEntity(
                address = userAddressNote.address,
                content = userAddressNote.addressNoteContent,
                lat = userAddressNote.lat,
                lng = userAddressNote.lng,
                userId = userAddressNote.userId
            )
        )
    }

    override suspend fun deleteUserData(userId: Int) = preferences.deleteUserData()
}