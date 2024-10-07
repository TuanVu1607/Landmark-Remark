package com.technology.landmarkremark.di.repository

import com.technology.landmarkremark.data.dao.AddressNoteDao
import com.technology.landmarkremark.data.dao.UserAddressNoteDao
import com.technology.landmarkremark.data.dao.UserDao
import com.technology.landmarkremark.data.repository.AddressNoteRepositoryImp
import com.technology.landmarkremark.data.repository.LoginRepositoryImp
import com.technology.landmarkremark.data.repository.MainRepositoryImp
import com.technology.landmarkremark.data.repository.MapRepositoryImp
import com.technology.landmarkremark.data.repository.SplashRepositoryImp
import com.technology.landmarkremark.domain.repository.AddressNoteRepository
import com.technology.landmarkremark.domain.repository.LoginRepository
import com.technology.landmarkremark.domain.repository.MainRepository
import com.technology.landmarkremark.domain.repository.MapRepository
import com.technology.landmarkremark.domain.repository.SplashRepository
import com.technology.landmarkremark.google_services.GoogleServicesApi
import com.technology.landmarkremark.preferences.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    internal fun provideSplashRepository(
        preferences: Preferences
    ): SplashRepository {
        return SplashRepositoryImp(preferences)
    }

    @Provides
    internal fun provideMainRepository(
        preferences: Preferences,
        addressNoteDao: AddressNoteDao,
    ): MainRepository {
        return MainRepositoryImp(preferences, addressNoteDao)
    }

    @Provides
    internal fun provideLoginRepository(
        userDao: UserDao,
        preferences: Preferences
    ): LoginRepository {
        return LoginRepositoryImp(userDao, preferences)
    }

    @Provides
    internal fun provideMapRepository(
        userDao: UserDao,
        addressNoteDao: AddressNoteDao,
        userAddressNoteDao: UserAddressNoteDao,
        googleServicesApi: GoogleServicesApi,
    ): MapRepository {
        return MapRepositoryImp(userDao, addressNoteDao, userAddressNoteDao, googleServicesApi)
    }

    @Provides
    internal fun provideAddressNoteRepository(
        addressNoteDao: AddressNoteDao
    ): AddressNoteRepository {
        return AddressNoteRepositoryImp(addressNoteDao)
    }
}