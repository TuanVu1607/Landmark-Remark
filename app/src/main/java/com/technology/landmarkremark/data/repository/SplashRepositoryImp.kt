package com.technology.landmarkremark.data.repository

import com.technology.landmarkremark.domain.repository.SplashRepository
import com.technology.landmarkremark.preferences.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashRepositoryImp @Inject constructor(private val preferenceManager: PreferenceManager) : SplashRepository {
    override suspend fun checkLogin(): Boolean = preferenceManager.isLogin()
}