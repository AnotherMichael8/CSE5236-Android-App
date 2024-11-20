package com.example.cse5236mobileapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.databinding.ActivityMapsBinding
import com.example.cse5236mobileapp.model.LocationTournamentAdapter
import com.example.cse5236mobileapp.model.OnTournamentClickListener
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.viewmodel.GeocoderViewModel
import com.example.cse5236mobileapp.utility.Internet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener, OnMapReadyCallback,
    OnRequestPermissionsResultCallback, OnTournamentClickListener,
    Internet.NetworkStateListener {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding

    private lateinit var internetMonitor: Internet

    private var geocoding: GeocoderViewModel? = null

    private var geocodeStore = listOf<TournamentIdentifier>()
    private lateinit var locationTournamentAdapter : LocationTournamentAdapter

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val TAG = "Maps Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        internetMonitor = Internet(this)
        internetMonitor.startMonitoring(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.location_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val rvPublicTournaments = findViewById<RecyclerView>(R.id.rvPublicTournaments)
        locationTournamentAdapter = LocationTournamentAdapter(this)
        rvPublicTournaments.adapter = locationTournamentAdapter
        rvPublicTournaments.layoutManager = LinearLayoutManager(this)

        if(geocoding != null){
            geocoding!!.publicTournamentLive.observe(this) { geocodes ->
                locationTournamentAdapter.updatePublicTournaments(geocodes)
                geocodeStore = geocodes
            }
        }

        val btBack = findViewById<Button>(R.id.btLocationBack)
        btBack.setOnClickListener {
            finish()
        }
        /*
        geocoding.geocoderLive.observe(this, Observer { geocodes ->
            geocodeStore = geocodes
            //plotMarkers(geocodes)
        })
         */
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            Log.i(TAG, "User rejected permission request")

        } else {
            binding.loadingSpinner.visibility = View.VISIBLE
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if(location != null) {
                    val userLat = location.latitude
                    val userLon = location.longitude
                    val currentLatLng = LatLng(userLat, userLon)
                    binding.loadingSpinner.visibility = View.GONE
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    locationTournamentAdapter.updateUserCurrentLocation(location)
                    Log.i(TAG, "Using last known location: <$userLat, $userLon>")
                } else {
                    Log.e(TAG, "Last known location is unavailable")
                    Toast.makeText(this, "Location unavailable. Please try again later.", Toast.LENGTH_LONG).show()
                }
            }
            enableMyLocation()
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.setOnMyLocationClickListener(this)
            Log.i(TAG, "Finished setting up user map")
        }

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        plotMarkers(geocodeStore)
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
        //locationTournamentAdapter.updateUserCurrentLocation(location)
    }


    private fun plotMarkers(geocodes: List<TournamentIdentifier>) {
        for (location in geocodes) {
            if(location.tournament.latLng != null) {
                mMap.addMarker(
                    MarkerOptions()
                        .position(location.tournament.latLng!!)
                        .title(location.tournament.tournamentName)
                )
            }
        }
    }

    override fun onTournamentClick(tournament: Tournament){
        if(tournament.latLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tournament.latLng!!, 15f))
            mMap.addMarker(MarkerOptions().position(tournament.latLng!!).title(tournament.tournamentName))
        }
    }

    // If network is still available then start or continue (not sure whether to start or continue) map functionality
    override fun onNetworkAvailable() {
        Log.i(TAG, "Network available")
        runOnUiThread {
            if (geocoding == null) {
                geocoding = GeocoderViewModel(this)
                geocoding!!.publicTournamentLive.observe(this) { geocodes ->
                    locationTournamentAdapter.updatePublicTournaments(geocodes)
                    geocodeStore = geocodes
                }
            }
            // If the map is already ready, re-plot the markers
            if (::mMap.isInitialized) {
                plotMarkers(geocodeStore)
            }
        }
    }

    // If network is lost then must halt map functionality before it crashes
    override fun onNetworkLost() {
        Log.i(TAG, "Network lost")
        Toast.makeText(this, "Network connection unavailable: Locations unable to be shown. Try reloading this activity later.", Toast.LENGTH_LONG).show()
        geocoding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Destroying MapsActivity")
        internetMonitor.stopMonitoring()
    }
}