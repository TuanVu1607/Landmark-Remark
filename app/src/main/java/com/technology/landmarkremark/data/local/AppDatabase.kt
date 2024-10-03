package com.technology.landmarkremark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.technology.landmarkremark.data.dao.AddressNoteDao
import com.technology.landmarkremark.data.dao.UserAddressNoteDao
import com.technology.landmarkremark.data.dao.UserDao
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [UserEntity::class, AddressNoteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressNoteDao(): AddressNoteDao
    abstract fun userAddressNoteDao(): UserAddressNoteDao
    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}