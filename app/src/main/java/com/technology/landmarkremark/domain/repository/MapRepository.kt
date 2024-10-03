package com.technology.landmarkremark.domain.repository

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.data.model.UserAddressNote

interface MapRepository {
    suspend fun getProfileUser(userId: Int): UserEntity

    fun getAddressNotes(): LiveData<List<AddressNoteEntity>>

    suspend fun addAddressNote(addressNote: AddressNoteEntity): Long

    suspend fun updateAddressNote(addressNote: AddressNoteEntity): Int

    fun searchAddressNote(searchQuery: String): LiveData<List<UserAddressNote>>

    suspend fun reverseGeocoding(lat: Double, lng: Double): MutableList<Address>?

    suspend fun reverseGeocodingNewVersion(lat: Double, lng: Double, geoListener: Geocoder.GeocodeListener)
}