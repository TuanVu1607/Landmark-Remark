package com.technology.landmarkremark.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.technology.landmarkremark.data.model.UserAddressNote

@Dao
interface UserAddressNoteDao {

    @Query(
        """
        SELECT 
            user_table.id AS userId,
            user_table.name AS userName,
            address_note_table.id AS addressNoteId,
            address_note_table.content AS addressNoteContent,
            address_note_table.address AS address,
            address_note_table.lat AS lat,
            address_note_table.lng AS lng
        FROM address_note_table
        LEFT JOIN user_table ON user_table.id = address_note_table.userId
    """
    )
    fun getAllUsersWithAddressNotes(): LiveData<List<UserAddressNote>>

    @Query(
        """
        SELECT 
            user_table.id AS userId,
            user_table.name AS userName,
            address_note_table.id AS addressNoteId,
            address_note_table.content AS addressNoteContent,
            address_note_table.address AS address,
            address_note_table.lat AS lat,
            address_note_table.lng AS lng
        FROM address_note_table
        LEFT JOIN user_table ON user_table.id = address_note_table.userId
        WHERE :searchQuery = '' OR user_table.name LIKE '%' || :searchQuery || '%' 
        OR address_note_table.address LIKE '%' || :searchQuery || '%'
        OR address_note_table.content LIKE '%' || :searchQuery || '%'
    """
    )
    fun searchUsersWithAddressNotes(searchQuery: String): LiveData<List<UserAddressNote>>
}