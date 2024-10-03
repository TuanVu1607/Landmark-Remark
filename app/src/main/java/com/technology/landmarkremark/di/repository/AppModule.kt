package com.technology.landmarkremark.di.repository

import android.content.Context
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
import com.technology.landmarkremark.google_services.GoogleServicesManager
import com.technology.landmarkremark.preferences.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    internal fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @Provides
    internal fun provideGoogleServicesManager(@ApplicationContext context: Context): GoogleServicesManager {
        return GoogleServicesManager(context)
    }

    @Provides
    internal fun provideSplashRepository(preferenceManager: PreferenceManager): SplashRepository {
        return SplashRepositoryImp(preferenceManager)
    }

    @Provides
    internal fun provideLoginRepository(
        userDao: UserDao,
        preferenceManager: PreferenceManager
    ): LoginRepository {
        return LoginRepositoryImp(userDao, preferenceManager)
    }

    @Provides
    internal fun provideMapRepository(
        userDao: UserDao,
        addressNoteDao: AddressNoteDao,
        userAddressNoteDao: UserAddressNoteDao,
        googleServicesManager: GoogleServicesManager,
    ): MapRepository {
        return MapRepositoryImp(userDao, addressNoteDao, userAddressNoteDao, googleServicesManager)
    }

    @Provides
    internal fun provideAddressNoteRepository(
        addressNoteDao: AddressNoteDao
    ): AddressNoteRepository {
        return AddressNoteRepositoryImp(addressNoteDao)
    }

    @Provides
    internal fun provideMainRepository(
        preferenceManager: PreferenceManager,
        addressNoteDao: AddressNoteDao,
    ): MainRepository {
        return MainRepositoryImp(preferenceManager, addressNoteDao)
    }
}