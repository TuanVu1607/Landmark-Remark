package com.technology.landmarkremark.data.repository

import com.technology.landmarkremark.data.dao.UserDao
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.domain.repository.LoginRepository
import com.technology.landmarkremark.common.preferences.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImp @Inject constructor(
    private val userDao: UserDao,
    private val preferences: Preferences
) : LoginRepository {
    override suspend fun loginUser(username: String) =
        userDao.loginUser(username)

    override suspend fun registerUser(user: UserEntity) = userDao.insert(user)

    override suspend fun saveUser(user: UserEntity) = preferences.setUserData(user)
}