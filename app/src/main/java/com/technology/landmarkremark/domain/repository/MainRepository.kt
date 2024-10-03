package com.technology.landmarkremark.domain.repository

import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.data.model.UserAddressNote

interface MainRepository {
    suspend fun getUserData(): UserEntity

    suspend fun createNote(userAddressNote: UserAddressNote): Long

    suspend fun deleteUserData(userId: Int)
}