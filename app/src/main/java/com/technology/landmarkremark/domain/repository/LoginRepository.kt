package com.technology.landmarkremark.domain.repository

import com.technology.landmarkremark.data.entity.UserEntity

interface LoginRepository {
    suspend fun loginUser(username:String): UserEntity?

    suspend fun registerUser(user: UserEntity): Long

    suspend fun saveUser(user: UserEntity)
}