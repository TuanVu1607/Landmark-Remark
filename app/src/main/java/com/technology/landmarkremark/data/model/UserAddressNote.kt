package com.technology.landmarkremark.data.model

import com.google.gson.annotations.SerializedName

data class UserAddressNote(
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("user_name")
    var userName: String = "",
    @SerializedName("address_note_id")
    var addressNoteId: Int = 0,
    @SerializedName("address_note_content")
    var addressNoteContent: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("lat")
    var lat: Double = 0.0,
    @SerializedName("lng")
    var lng: Double = 0.0
)
