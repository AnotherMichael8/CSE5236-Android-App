package com.example.cse5236mobileapp.model.viewmodel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class GeocoderViewModel(val context: Context) {

    private var firestore = Firebase.firestore
    private var user = FirebaseAuth.getInstance().currentUser?.email

    val geocoderLive: MutableLiveData<Map<Tournament, LatLng>> by lazy {
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

    fun addressGeocoded(location: String): LatLng? {
        val geo = Geocoder(context)
        val results = geo.getFromLocationName(location, 1)
        if (results != null) {
            if (results.size > 0) {
                val long = results[0].longitude
                val lat = results[0].latitude
                return (LatLng(lat, long))
            } else {
                return null
            }
        } else {
            return null
        }
    }

    fun getDistance(userLocation: LatLng, tourneyLocation: LatLng): Double {
        var results = floatArrayOf()
        Location.distanceBetween(userLocation.latitude, userLocation.longitude, tourneyLocation.latitude, tourneyLocation.longitude, results)
        return metersToMiles(results[0])
    }

    private fun metersToMiles(distanceMeters: Float): Double {
        return distanceMeters * .000621371
    }
}