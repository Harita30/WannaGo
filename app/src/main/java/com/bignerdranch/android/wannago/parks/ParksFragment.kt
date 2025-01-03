package com.bignerdranch.android.wannago.parks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.wannago.GoViewModel
import com.bignerdranch.android.WannaGo2.databinding.FragmentParksBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch

//private const val TAG = "ParksFragment"
private const val DEFAULT_ZOOM = 15f

class ParksFragment: Fragment(), OnMapReadyCallback {
    private val goViewModel: GoViewModel by viewModels()
    private var _binding: FragmentParksBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "binding cannot be created. Is view created?"
        }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var locationPermissionGranted: Boolean = false

    private lateinit var map: GoogleMap
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    @SuppressLint("MissingPermission")
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        locationPermissionGranted = permissions.entries.all {
            it.value
        }

        if (locationPermissionGranted) {
            // starts requesting for location updates
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParksBinding.inflate(inflater, container, false)
        if (!locationEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token)
                .addOnSuccessListener { location: Location? ->
                    updateMapLocation(location)
                    updateMapUI()
                }
        }
        else { // launch the dialog requesting for permissions
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.latLongRecyclerView.layoutManager = LinearLayoutManager(context)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                goViewModel.getCoords("parks").collect {coordList1 ->

                    binding.latLongRecyclerView.adapter = ParksAdapter(coordList1)
                    coordList1.forEach {
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.latitude,it.longitude))
                                .visible(true)
                        )
                    }
                }
            }
        }

    }

    private fun locationEnabled(): Boolean {
        val locationManager: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        updateMapUI()
        binding.mapView.onResume()
        map.setOnMapClickListener {latLongValues ->
            goViewModel.setCoords(latLongValues.latitude,latLongValues.longitude,"parks",Geocoder(requireContext()))
            map.addMarker(
                MarkerOptions()
                    .position(latLongValues)
                    .visible(true)
            )
        }
    }

    private fun updateMapUI() {
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun updateMapLocation(location: Location?) {
        if (!locationPermissionGranted || location == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(defaultLocation.latitude, defaultLocation.longitude), DEFAULT_ZOOM))
            return
        }

        try {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), DEFAULT_ZOOM)
            )
        }
        catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}