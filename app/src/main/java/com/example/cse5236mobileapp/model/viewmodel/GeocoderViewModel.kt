package com.example.cse5236mobileapp.model.viewmodel

import android.content.Context
import android.location.Geocoder
import android.util.Log
//import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import java.io.IOException

class GeocoderViewModel(val context: Context) {

    companion object {
        private const val TAG = "GeocoderViewModel"
    }

    private var firestore = Firebase.firestore
    private var user = FirebaseAuth.getInstance().currentUser?.email

    private val geocoderLive: MutableLiveData<Map<Tournament, LatLng>> by lazy {
        MutableLiveData<Map<Tournament, LatLng>>()
    }
    val publicTournamentLive: MutableLiveData<List<TournamentIdentifier>> by lazy {
        MutableLiveData<List<TournamentIdentifier>>()
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
                    publicTournamentLive.value = emptyList()
                    return@addSnapshotListener
                }

                val publicTourneys = mutableListOf<TournamentIdentifier>()
                //val tournamentGeocoded = mutableMapOf<Tournament, LatLng>()
                if (documents != null) {
                    for (doc in documents) {
                        val tourneyIdentifierId= doc.id
                        val currentTourney = doc.toObject<Tournament>()
                        val tournamentInfo = TournamentIdentifier(tournamentId = tourneyIdentifierId, tournament = currentTourney)
                        val geocode = addressGeocoded(currentTourney.address)

                        // TODO: Check if tourney has space in it
                        if(geocode != null) {
                            if (!currentTourney.isTournamentFull()) {
                                if (!currentTourney.isUserAPlayer(user)) {
                                    tournamentInfo.tournament.latLng = geocode
                                    publicTourneys.add(tournamentInfo)
                                }
                            }
                        }
                    }
                }
                //geocoderLive.value = tournamentGeocoded
                publicTournamentLive.value = publicTourneys
            }
    }

    private fun addressGeocoded(location: String): LatLng? {
        return try {
            val geocoder = Geocoder(context)
            val results = geocoder.getFromLocationName(location, 1)
            if (results.isNullOrEmpty()) {
                Log.e(TAG, "No results found for the address: $location")
                null
            } else {
                val location = results[0]
                LatLng(location.latitude, location.longitude)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Geocoder service unavailable: ${e.message}")
            null
        }
    }
    //TODO: can be deleted
//    fun getDistance(userLocation: LatLng, tourneyLocation: LatLng): Double {
//        var results = floatArrayOf()
//        Location.distanceBetween(userLocation.latitude, userLocation.longitude, tourneyLocation.latitude, tourneyLocation.longitude, results)
//        return metersToMiles(results[0])
//    }
//
//    private fun metersToMiles(distanceMeters: Float): Double {
//        return distanceMeters * .000621371
//    }
}