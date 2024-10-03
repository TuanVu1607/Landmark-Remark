package com.technology.landmarkremark.google_services

import android.content.Context
import android.location.Address
import android.location.Geocoder

interface GoogleServiceApi {
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