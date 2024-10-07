package com.technology.landmarkremark.data.repository

import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.technology.landmarkremark.data.dao.AddressNoteDao
import com.technology.landmarkremark.data.dao.UserAddressNoteDao
import com.technology.landmarkremark.data.dao.UserDao
import com.technology.landmarkremark.data.entity.AddressNoteEntity
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.domain.repository.MapRepository
import com.technology.landmarkremark.google_services.GoogleServicesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepositoryImp @Inject constructor(
    private val userDao: UserDao,
    private val addressNoteDao: AddressNoteDao,
    private val userAddressNoteDao: UserAddressNoteDao,
    private val googleServicesApi: GoogleServicesApi,
) : MapRepository {
    override suspend fun getProfileUser(userId: Int) = userDao.getUserById(userId)

    override fun getAddressNotes() = addressNoteDao.getAllData()

    override suspend fun addAddressNote(addressNote: AddressNoteEntity) =
        addressNoteDao.insert(addressNote)

    override suspend fun updateAddressNote(addressNote: AddressNoteEntity) =
        addressNoteDao.update(addressNote)

    override fun searchAddressNote(searchQuery: String): LiveData<List<UserAddressNote>> =
        userAddressNoteDao.searchUsersWithAddressNotes(searchQuery)

    override suspend fun reverseGeocoding(lat: Double, lng: Double) =
        googleServicesApi.getReverseGeocoding(lat, lng, 1)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun reverseGeocodingNewVersion(
        lat: Double,
        lng: Double,
        geoListener: Geocoder.GeocodeListener
    ) = googleServicesApi.getReverseGeocodingNewVersion(lat, lng, 1, geoListener)
}