package com.example.cse5236mobileapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.ui.fragment.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class JoinActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    }
    override fun onMapReady(googleMap: GoogleMap) {
        TODO("Not yet implemented")
    }

}