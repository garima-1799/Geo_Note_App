package com.example.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    var maps: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentMarker: Marker? = null
    var shouldFollowUser: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val autocompleteFragment = AutocompleteSupportFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.autocomplete_container, autocompleteFragment)
            .commit()

        // Configure which data fields to return
        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        )

        // Handle place selection
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Toast.makeText(this@MainActivity, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    maps?.clear()
                    maps?.addMarker(MarkerOptions().position(latLng).title(place.name))
                    maps?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        })
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyCvRqMCadioMVFGF1u89l2wTA6gS6hOjNw")
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        checkPermissionAndStart()
    }

    private fun checkPermissionAndStart() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            startLocationUpdates()
        }
    }

    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        maps?.isMyLocationEnabled = true

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            20000 // update every
        ).setMinUpdateIntervalMillis(100000)
            .build()

         locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    // Move marker
                    if (!shouldFollowUser) return
                    if (currentMarker == null) {
                        currentMarker = maps?.addMarker(
                            MarkerOptions().position(latLng).title("You are here")
                        )
                        shouldFollowUser = false;
                        maps?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                    } else {
                        currentMarker?.position = latLng
                        maps?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }
}