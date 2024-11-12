package com.example.cse5236mobileapp.model.viewmodel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.cse5236mobileapp.model.Tournament
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class GeocoderViewModel(val context: Context) {

    private var firestore = Firebase.firestore

    val geocoderLive: MutableLiveData<Map<Tournament, LatLng>> by lazy {
        MutableLiveData<Map<Tournament, LatLng>>()
    }

    init {
        getTourneyGeocodes()
        firestore = FirebaseFirestore.getInstance()
    }


    private fun getTourneyGeocodes() {
        // Querying non-private tournaments
        firestore.collection("Tournaments").whereEqualTo("private", false)
            .addSnapshotListener { documents, exception ->
                if (exception != null) {
                    geocoderLive.value = emptyMap()
                    return@addSnapshotListener
                }

                val tournamentGeocoded = mutableMapOf<Tournament, LatLng>()
                if (documents != null) {
                    for (doc in documents) {
                        val currentTourney = doc.toObject<Tournament>()
                        var geocode = addressGeocoded(currentTourney.address)
                        if (geocode != null) {
                            tournamentGeocoded[currentTourney] = geocode
                        }
                    }
                }
                geocoderLive.value = tournamentGeocoded
            }
    }

    fun addressGeocoded(location: String): LatLng? {
        val geo = Geocoder(context)
        val results = geo.getFromLocationName(location, 1)
        if (results != null) {
            if (results.size > 0) {
                val long = results[0].longitude
                val lat = results[0].latitude
                return(LatLng(lat, long))
            }
            else {
                return null
            }
        }
        else {
            return null
        }
    }
}