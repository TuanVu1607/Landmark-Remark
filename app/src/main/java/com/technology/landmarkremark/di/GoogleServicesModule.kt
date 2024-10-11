package com.technology.landmarkremark.di

import android.content.Context
import com.technology.landmarkremark.common.google_services.GoogleServicesApi
import com.technology.landmarkremark.common.google_services.GoogleServicesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleServicesModule {
    @Provides
    @Singleton
    fun provideGoogleServices(@ApplicationContext context: Context): GoogleServicesApi =
        GoogleServicesManager(context)
}