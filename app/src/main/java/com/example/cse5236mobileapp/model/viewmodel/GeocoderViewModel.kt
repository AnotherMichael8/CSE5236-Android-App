package com.example.cse5236mobileapp.model.viewmodel

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import java.io.IOException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GeocoderViewModel(val context: Context): ViewModel() {

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
    private var listenerRegistration : ListenerRegistration? = null;

    init {
        getTourneyGeocodes()
        firestore = FirebaseFirestore.getInstance()
    }


    private fun getTourneyGeocodes() {
        // Querying non-private tournaments
        listenerRegistration = firestore.collection("Tournaments").whereEqualTo("private", false)
            .addSnapshotListener { documents, exception ->
                if (exception != null) {
                    geocoderLive.value = emptyMap()
                    publicTournamentLive.value = emptyList()
                    return@addSnapshotListener
                }

                val publicTourneys = mutableListOf<TournamentIdentifier>()
                //val tournamentGeocoded = mutableMapOf<Tournament, LatLng>()
                if (documents != null) {

                    viewModelScope.launch {

                        // Documents are being processed concurrently now
                        val deferredResults = documents.map { doc ->
                            async {
                                val tourneyIdentifierId = doc.id
                                val currentTourney = doc.toObject<Tournament>()
                                val tournamentInfo = TournamentIdentifier(
                                    tournamentId = tourneyIdentifierId,
                                    tournament = currentTourney
                                )
                                val geocode = addressGeocoded(currentTourney.address)

                                if (geocode != null &&
                                    !currentTourney.isTournamentFull() &&
                                    !currentTourney.isUserAPlayer(user)) {
                                    tournamentInfo.tournament.latLng = geocode
                                    tournamentInfo
                                } else {
                                    null
                                }
                            }
                        }

                        // Wait for all coroutines to complete
                        val results = deferredResults.mapNotNull { it.await() }
                        publicTourneys.addAll(results)

                        // Update LiveData with the results
                        publicTournamentLive.value = publicTourneys
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
    fun destroyViewModel()
    {
        listenerRegistration?.remove()
        listenerRegistration = null
        geocoderLive.value = null
        publicTournamentLive.value = null
    }
}