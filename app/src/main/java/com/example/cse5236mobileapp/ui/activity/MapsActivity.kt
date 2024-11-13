package com.example.cse5236mobileapp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.example.cse5236mobileapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.cse5236mobileapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

//import retrofit2.Retrofit


class MapsActivity : AppCompatActivity(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener, OnMapReadyCallback,
    OnRequestPermissionsResultCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding
    private var ohioStadiumLatLng: LatLng = LatLng(40.001633, -83.019707)

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val TAG = "Maps Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
//        val startLocation = "328+West+Lane+Ave,+Columbus,+Ohio"
//        val endLocation = "Ohio+Stadium"
//        var httpsRequest = "https://maps.googleapis.com/maps/api/directions/json?origin=START_LOCATION&destination=END_LOCATION&key=YOUR_API_KEY\n"
//
//        val apiKey = try {
//            // TODO: MIGHT need to be changed if we make secrets.properties secret
//            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
//            ai.metaData.getString("com.google.android.geo.API_KEY")
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//            null
//        }
//
//        if(apiKey != null){
//            httpsRequest = "https://maps.googleapis.com/maps/api/directions/json?origin=$startLocation&destination=$endLocation&key=$apiKey\n"
//            Log.i(TAG, "Successful https build: $httpsRequest")
//            val client = OkHttpClient()
//            val request = Request.Builder()
//                .url(httpsRequest)
//                .build()
//
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    e.printStackTrace()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    response.body?.let {
//                        val responseData = it.string()
//                        Log.i(TAG, "Successful response: $responseData")
//                    }
//                }
//            })
//        } else {
//            Log.e(TAG, "Failed https build")
//        }



        if(ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            Log.i(TAG, "User rejected permission request")

        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if(location != null) {
                    val userLat = location.latitude
                    val userLon = location.longitude
                    val ohioStadiumMarker = mMap.addMarker(
                        MarkerOptions()
                            .title("Ohio Stadium")
                            .position(ohioStadiumLatLng)
                    )
                    val currentLatLng = LatLng(userLat, userLon)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                    val route = Retrofit
                    Log.i(TAG, "User Location retrieved: <$userLat, $userLon>")
                } else {
                    Log.e(TAG, "No user location")
                }
            }
            enableMyLocation()
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.setOnMyLocationClickListener(this)
            Log.i(TAG, "Finished setting up user map")
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location != null) {
                val userLat = location.latitude
                val userLon = location.longitude
                val currentLatLng = LatLng(userLat, userLon)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                Log.i(TAG, "User Location retrieved: <$userLat, $userLon>")
            } else {
                Log.e(TAG, "No user location")
            }
        }
        enableMyLocation()
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        Log.i(TAG, "Finished setting up user map")
    }

}