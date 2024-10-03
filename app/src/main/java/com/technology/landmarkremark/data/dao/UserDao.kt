package com.technology.landmarkremark.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.technology.landmarkremark.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity): Int

    @Delete
    suspend fun delete(user: UserEntity): Int

    @Query("SELECT * FROM user_table")
    fun getAllData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserById(id: Int): UserEntity

    @Query("SELECT * FROM user_table WHERE name = :name")
    fun loginUser(name: String): UserEntity?

    @Query("UPDATE user_table SET name = :newUserName WHERE id = :userId")
    fun updateUserById(userId: Int, newUserName: String): Int

    @Query("DELETE FROM user_table WHERE id = :userId")
    fun deleteUserById(userId: Int): Int

    @Query("SELECT * FROM user_table WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchUser(searchQuery: String): LiveData<List<UserEntity>>
}