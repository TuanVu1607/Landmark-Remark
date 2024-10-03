package com.technology.landmarkremark.google_services

import android.content.Context
import android.location.Geocoder

abstract class GoogleServices(context: Context) {
    protected val geocoder: Geocoder by lazy {
        Geocoder(context)
    }
}