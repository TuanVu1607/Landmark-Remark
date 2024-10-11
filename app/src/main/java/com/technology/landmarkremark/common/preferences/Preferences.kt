package com.technology.landmarkremark.common.preferences

import com.technology.landmarkremark.data.entity.UserEntity

interface Preferences {
    fun getAccessToken(): String

    fun setAccessToken(accessToken: String)

    fun deleteAccessToken()

    fun getUserData(): UserEntity

    fun setUserData(userData: UserEntity)

    fun deleteUserData()

    fun isLogin(): Boolean
}