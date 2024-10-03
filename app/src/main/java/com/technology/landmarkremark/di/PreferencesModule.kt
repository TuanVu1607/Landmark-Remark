package com.technology.landmarkremark.di

import android.content.Context
import com.technology.landmarkremark.preferences.PreferenceManager
import com.technology.landmarkremark.preferences.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): Preferences =
        PreferenceManager(context)
}