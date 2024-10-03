package com.technology.landmarkremark.di

import android.app.Application
import androidx.room.Room
import com.technology.landmarkremark.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: AppDatabase.Callback): AppDatabase{
        return Room.databaseBuilder(application, AppDatabase::class.java, "landmark_remark_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Provides
    fun provideAddressNoteDao(db: AppDatabase) = db.addressNoteDao()

    @Provides
    fun provideUserAddressNoteDao(db: AppDatabase) = db.userAddressNoteDao()
}