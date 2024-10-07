package com.technology.landmarkremark.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.technology.landmarkremark.data.entity.AddressNoteEntity

@Dao
interface AddressNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(addressNote: AddressNoteEntity): Long

    @Update
    suspend fun update(addressNote: AddressNoteEntity): Int

    @Delete
    suspend fun delete(addressNote: AddressNoteEntity): Int

    @Query("SELECT * FROM address_note_table")
    fun getAllData(): LiveData<List<AddressNoteEntity>>

    @Query("SELECT * FROM address_note_table WHERE userId = :userId")
    fun getUserAddressNoteHistory(userId: Int): LiveData<List<AddressNoteEntity>>

    @Query("SELECT * FROM address_note_table WHERE :searchQuery = '' OR address LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
    fun searchAddressNote(searchQuery: String): LiveData<List<AddressNoteEntity>>
}