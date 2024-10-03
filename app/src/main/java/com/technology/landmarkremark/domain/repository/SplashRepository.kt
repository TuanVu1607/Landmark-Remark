package com.technology.landmarkremark.domain.repository

interface SplashRepository {
    suspend fun checkLogin(): Boolean
}