package com.technology.landmarkremark.viewmodel.map

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.technology.landmarkremark.R
import com.technology.landmarkremark.data.entity.UserEntity
import com.technology.landmarkremark.data.model.AddressLocation
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.domain.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {
    private val TAG = MapViewModel::class.java.simpleName

    val myLocationData = MutableLiveData<Resource<Address>>()
    var myCurrentMarker: Marker? = null
    val searchKey: MutableLiveData<String> = MutableLiveData("")
    val usersAddressNoteData: LiveData<List<UserAddressNote>> = searchKey.switchMap {text->
        mapRepository.searchAddressNote(text)
    }

    init {
        Log.i(TAG, "initViewModel()")
    }

    fun reverseGeocodeMyLocation(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mapRepository.reverseGeocodingNewVersion(
                    location.latitude,
                    location.longitude, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isEmpty()) {
                                myLocationData.postValue(Resource.Error("Không tìm thấy địa chỉ"))
                            } else {
                                myLocationData.postValue(Resource.Success(addresses[0]))
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            if (errorMessage != null) {
                                myLocationData.postValue(Resource.Error(errorMessage))
                            } else {
                                myLocationData.postValue(Resource.Error("Không tìm thấy địa chỉ"))
                            }
                        }
                    }
                )
            } else {
                val response = mapRepository.reverseGeocoding(location.latitude, location.longitude)
                if (!response.isNullOrEmpty()) {
                    myLocationData.postValue(Resource.Success(response[0]))
                } else {
                    myLocationData.postValue(Resource.Error("Không tìm thấy địa chỉ"))
                }
            }
        } catch (e: Exception) {
            if (e.message != null) myLocationData.postValue(Resource.Error(e.message!!))
            else if (e.localizedMessage != null) myLocationData.postValue(Resource.Error(e.localizedMessage!!))
            else myLocationData.postValue(Resource.Error("Google Service Api lỗi"))
        }
    }

    private fun getBitmapFromDrawable(resId: Int, resources: Resources): Bitmap? {
        var bitmap: Bitmap? = null
        val drawable = ResourcesCompat.getDrawable(resources, resId, null)
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
        return bitmap
    }

    fun handleSetMyMarkerLocation(
        map: GoogleMap?,
        lat: Double,
        lng: Double,
        addressText: String,
    ) {
        val latLng = LatLng(lat, lng)
        val icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        var title = "My current location"
        var snippet: String? = "My current location"
        if (addressText.isNotEmpty()) {
            title = "Địa chỉ: $addressText"
            snippet = null
        }
        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(icon)
            .title(title)
        snippet?.let { markerOptions.snippet(it) }
        myCurrentMarker = map?.addMarker(markerOptions)
    }

    fun handleSetUserMarkerLocation(
        userAddressNote: UserAddressNote,
        map: GoogleMap?,
        resources: Resources,
        userData: UserEntity?
    ) {
        var userName = userAddressNote.userName
        var icon = BitmapDescriptorFactory.fromBitmap(
            this.getBitmapFromDrawable(
                R.drawable.ic_user,
                resources
            )!!
        )
        if (userAddressNote.userId == userData?.id) {
            userName = userData.name + "(me)"
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        }
        val marker = MarkerOptions()
            .position(LatLng(userAddressNote.lat, userAddressNote.lng))
            .title("Địa chỉ: ${userAddressNote.address}")
            .snippet(
                "Username: $userName\n" +
                        "Note: ${userAddressNote.addressNoteContent}"
            )
            .icon(icon)
        map?.addMarker(marker)
    }

    fun setTextSearch(text:String) = searchKey.postValue(text)
}