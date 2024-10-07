package com.technology.landmarkremark.data.repository

import com.technology.landmarkremark.domain.repository.SplashRepository
import com.technology.landmarkremark.preferences.PreferenceManager
import com.technology.landmarkremark.preferences.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashRepositoryImp @Inject constructor(private val preferences:Preferences) : SplashRepository {
    override suspend fun checkLogin(): Boolean = preferences.isLogin()
}