package com.technology.landmarkremark.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "address_note_table")
data class AddressNoteEntity (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("address")
    var address: String = "",
    @SerializedName("lat")
    var lat: Double = 0.0,
    @SerializedName("lng")
    var lng: Double = 0.0,
    @SerializedName("content")
    var content: String = "",
    @SerializedName("user_id")
    var userId: Int = 0,
)