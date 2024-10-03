package com.technology.landmarkremark.ui.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.technology.landmarkremark.R
import com.technology.landmarkremark.common.extensions.setOnSingleClickListener
import com.technology.landmarkremark.common.extensions.toast
import com.technology.landmarkremark.constants.GOOGLE_SERVICE_API_ERROR
import com.technology.landmarkremark.data.model.AddressLocation
import com.technology.landmarkremark.data.model.Resource
import com.technology.landmarkremark.data.model.UserAddressNote
import com.technology.landmarkremark.databinding.FragmentMapBinding
import com.technology.landmarkremark.interfaces.ItemClickListener
import com.technology.landmarkremark.ui.adapter.CustomInfoWindowAdapter
import com.technology.landmarkremark.ui.adapter.UserAddressNoteAdapter
import com.technology.landmarkremark.ui.base.BaseFragment
import com.technology.landmarkremark.viewmodel.main.MainViewModel
import com.technology.landmarkremark.viewmodel.map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback,
    ItemClickListener {
    private val TAG = MapFragment::class.java.simpleName

    private var map: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private var userAddressNoteAdapter: UserAddressNoteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")
        binding.mapViewModel = viewModel
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initMap()
        initAdapter()
        eventHandle()
        observeMyLocationData()
        observeUserAddressNoteData()
    }

    private fun initAdapter() {
        context?.let {
            userAddressNoteAdapter = UserAddressNoteAdapter(this)
            binding.searchList.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.searchList.addItemDecoration(
                DividerItemDecoration(
                    binding.searchList.context,
                    RecyclerView.VERTICAL
                )
            )
            binding.searchList.adapter = userAddressNoteAdapter
        }
    }

    private fun eventHandle() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.searchList.isVisible = !viewModel.searchKey.value.isNullOrEmpty()
                newText?.let { viewModel.setTextSearch(it) }
                Log.i(TAG, "onQueryTextChange: $newText")
                return false
            }
        })
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            binding.searchList.isVisible = hasFocus && !viewModel.searchKey.value.isNullOrEmpty()
        }
        binding.fabMyLocation.setOnSingleClickListener {
            enableMyLocation { getMyLocation() }
        }
    }

    private fun observeUserAddressNoteData() {
        viewModel.usersAddressNoteData.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                result.let { dataList ->
                    userAddressNoteAdapter?.differ?.submitList(dataList)
                    map?.clear()
                    dataList.forEach { userAddressNote ->
                        viewModel.handleSetUserMarkerLocation(
                            userAddressNote,
                            map,
                            resources,
                            mainViewModel.userData
                        )
                    }
                    if (viewModel.searchKey.value.isNullOrEmpty()) {
                        mainViewModel.currentLocation?.let { location ->
                            viewModel.handleSetMyMarkerLocation(
                                map,
                                location.lat,
                                location.lng,
                                location.address
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeMyLocationData() {
        viewModel.myLocationData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Loading Fetch Google Service Api")
                }

                is Resource.Error -> {
                    result.message?.let { errorMess ->
                        {
                            Log.e(GOOGLE_SERVICE_API_ERROR, errorMess)
                            context?.toast(errorMess)
                        }
                    }
                }

                is Resource.Success -> {
                    result.data?.let { address ->
                        mainViewModel.currentLocation?.let { location ->
                            location.address = address.getAddressLine(0)
                            jumpToMyLocation(
                                location.lat, location.lng,
                                address.getAddressLine(0),
                                false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initMap() {
        // Async map
        val supportFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        supportFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                getMyLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                getMyLocation()
            }

            else -> {
                // No location access granted.
                context?.toast("Please allow location permission")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // When map is loaded
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.apply {
            isZoomGesturesEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
            isScrollGesturesEnabled = true
            isMyLocationButtonEnabled = true
        }
        context?.let { googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(it)) }
        enableMyLocation { getMyLocation() }
    }

    private fun enableMyLocation(function: (() -> Unit)) {
        context?.let {
            // [START maps_check_location_permission]
            // 1. Check if permissions are granted, if so, enable the my location layer
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                function()
                return
            }

            // 2. Otherwise, request permission
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            // [END maps_check_location_permission]
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        fusedLocationClient?.apply {
            val task = this.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    mainViewModel.currentLocation =
                        AddressLocation(location.latitude, location.longitude, "")
                    viewModel.reverseGeocodeMyLocation(location)
                    jumpToMyLocation(location.latitude, location.longitude)
                }
            }
            task.addOnFailureListener { error ->
                Log.e(TAG, "getMyLocationError: ${error.message}")
                error.message?.let {
                    context?.toast(it)
                }
            }
        }
    }

    private fun jumpToMyLocation(
        lat: Double,
        lng: Double,
        addressText: String = "",
        isJump: Boolean = true
    ) {
        if (viewModel.myCurrentMarker != null) viewModel.myCurrentMarker?.remove()
        val latLng = LatLng(lat, lng)
        if (isJump) map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        viewModel.handleSetMyMarkerLocation(
            map,
            lat,
            lng,
            addressText
        )
    }

    private fun jumpToLocation(lat: Double, lng: Double){
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f))
    }

    override fun onClick(objects: Any?) {
        val data = objects as UserAddressNote
        jumpToLocation(data.lat, data.lng)
    }
}