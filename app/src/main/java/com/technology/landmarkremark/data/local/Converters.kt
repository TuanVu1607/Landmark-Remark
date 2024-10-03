package com.technology.landmarkremark.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.entity.UserEntity
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date {
        return value?.let { Date(it) } ?: Date(System.currentTimeMillis())
    }

    @TypeConverter
    fun toTimestamp(value: Date?): Long {
        return value?.let { value.time } ?: System.currentTimeMillis()
    }

    @TypeConverter
    fun fromUserJson(value: UserEntity?): String {
        return value?.let { Gson().toJson(value) } ?: ""
    }

    @TypeConverter
    fun toUserJson(value: String?): UserEntity {
        return value?.let { Gson().fromJson(it, UserEntity::class.java) } ?: UserEntity()
    }

    @TypeConverter
    fun fromAddressNoteJson(value: AddressNoteEntity?): String {
        return value?.let { Gson().toJson(value) } ?: ""
    }

    @TypeConverter
    fun toAddressNoteJson(value: String?): AddressNoteEntity {
        return value?.let { Gson().fromJson(it, AddressNoteEntity::class.java) }
            ?: AddressNoteEntity()
    }

}