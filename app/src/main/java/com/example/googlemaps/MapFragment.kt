package com.example.googlemaps

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapFragment : Fragment() , OnMapReadyCallback{
    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentMarker: Marker? = null
    private var shouldFollowUser = true
    private lateinit var viewModel: SearchViewModel
    private var searchedMarker: Marker? = null
    private val savedMarkers = mutableListOf<Marker>()
    private var isMapReady = false
    private var latestPlaces: List<GeoNote> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlacesAutocomplete()
        setupMap()
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val appDatabase = AppDatabase.getInstance(requireContext())
        val repository = GeoNoteRepository(appDatabase.geoNoteDao())
        viewModel = SearchViewModel(repository)
        lifecycleScope.launchWhenStarted {
            viewModel.savedPlaces.collect { places ->
                latestPlaces = places
                if (isMapReady) {
                    renderSavedPlaces(places)
                }
            }
        }
    }
    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map)
                    as SupportMapFragment

        mapFragment.getMapAsync(this)

        map?.setOnMapLongClickListener { latLng ->
            showSaveDialog(latLng)
        }

    }
    private fun showSaveDialog(latLng: LatLng) {
        val view = layoutInflater.inflate(R.layout.dialog_save_place, null)
        val titleEt = view.findViewById<EditText>(R.id.name_TV)
        val descEt = view.findViewById<EditText>(R.id.des_TV)

        AlertDialog.Builder(requireContext())
            .setTitle("Save place")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val title = titleEt.text.toString().ifBlank { "Saved Place" }
                val desc = descEt.text.toString()

                viewModel.savePlace(
                    title,
                    desc,
                    latLng.latitude,
                    latLng.longitude
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        isMapReady = true

        checkPermissionAndStart()
        renderSavedPlaces(latestPlaces)

        map?.setOnMarkerClickListener { marker ->
            if (marker == searchedMarker) {
                showSaveDialog(marker.position)
                true
            } else {
                false
            }
        }
        map?.setOnInfoWindowClickListener { marker ->
            val place = marker.tag as? GeoNote ?: return@setOnInfoWindowClickListener
            showEditDialog(place)
        }

    }
    private fun renderSavedPlaces(places: List<GeoNote>) {
        if (!isMapReady || places.isEmpty()) return

        savedMarkers.forEach { it.remove() }
        savedMarkers.clear()

        val boundsBuilder = LatLngBounds.Builder()

        places.forEach { place ->
            val latLng = LatLng(place.latitude, place.longitude)

            val marker = map?.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(place.title)
                    .snippet("Tap to edit")
            )
            marker?.tag = place
            marker?.let {
                savedMarkers.add(it)
                boundsBuilder.include(latLng)
            }
        }

        if (places.size > 1) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 150)
            )
        } else {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(places[0].latitude, places[0].longitude),
                    15f
                )
            )
        }
    }
    private fun showEditDialog(place: GeoNote) {
        val view = layoutInflater.inflate(R.layout.dialog_save_place, null)

        val titleEt = view.findViewById<EditText>(R.id.name_TV)
        val descEt = view.findViewById<EditText>(R.id.des_TV)

        // Pre-fill
        titleEt.setText(place.title)
        descEt.setText(place.description)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit place")
            .setView(view)

            .setPositiveButton("Update") { _, _ ->
                viewModel.updatePlace(
                    place.copy(
                        title = titleEt.text.toString(),
                        description = descEt.text.toString()
                    )
                )
            }

            .setNegativeButton("Delete") { _, _ ->
                viewModel.deletePlace(place)
            }

            .setNeutralButton("Cancel", null)
            .show()
    }

    private fun setupPlacesAutocomplete() {
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(
            object : PlaceSelectionListener {
                override fun onError(status: Status) {
                    Toast.makeText(
                        requireContext(),
                        status.statusMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPlaceSelected(place: Place) {
                    place.latLng?.let { latLng ->
                        searchedMarker = map?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(place.name)
                        )

                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        )
                    }
                }
            }
        )
    }

    private fun checkPermissionAndStart() {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            startLocationUpdates()
        }
    }

    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        map?.isMyLocationEnabled = true

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            20000
        ).setMinUpdateIntervalMillis(10000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    if (!shouldFollowUser) return

                    val latLng =
                        LatLng(location.latitude, location.longitude)

                    if (currentMarker == null) {
                        currentMarker = map?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("You are here")
                        )
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                        )
                        shouldFollowUser = false
                    } else {
                        currentMarker?.position = latLng
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
        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        }
    }
}
