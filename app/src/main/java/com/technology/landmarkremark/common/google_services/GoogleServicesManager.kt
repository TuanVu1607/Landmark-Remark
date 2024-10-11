package com.technology.landmarkremark.common.google_services

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

class GoogleServicesManager @Inject constructor(context: Context) : GoogleServices(context),
    GoogleServicesApi {
    override suspend fun getReverseGeocoding(
        lat: Double,
        lng: Double,
        maxResult: Int
    ): MutableList<Address>? = geocoder.getFromLocation(lat, lng, maxResult)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getReverseGeocodingNewVersion(
        lat: Double,
        lng: Double,
        maxResult: Int,
        geoListener: Geocoder.GeocodeListener
    ) = geocoder.getFromLocation(lat, lng, maxResult, geoListener)

}