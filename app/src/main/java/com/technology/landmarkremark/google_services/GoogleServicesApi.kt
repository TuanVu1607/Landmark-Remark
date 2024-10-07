package com.technology.landmarkremark.google_services

import android.location.Address
import android.location.Geocoder

interface GoogleServicesApi {
    suspend fun getReverseGeocoding(
        lat: Double,
        lng: Double,
        maxResult: Int
    ): MutableList<Address>?

    suspend fun getReverseGeocodingNewVersion(
        lat: Double,
        lng: Double,
        maxResult: Int,
        geoListener: Geocoder.GeocodeListener
    )
}