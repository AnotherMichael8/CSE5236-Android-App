package com.example.cse5236mobileapp.model.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.Tournament
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class TournamentGamesViewModel(private val tournamentId: String) {

    private var firestore = Firebase.firestore

    val tournamentLive: MutableLiveData<Tournament> by lazy {
        MutableLiveData<Tournament>()
    }
    val tournamentGamesLive: MutableLiveData<List<Game>> by lazy {
        MutableLiveData<List<Game>>()
    }

    init {
        firestore = FirebaseFirestore.getInstance()
        loadTournament()
        loadGameInTourney()
    }

    private fun loadTournament() {
        firestore.collection("Tournaments").document(tournamentId).addSnapshotListener { document, exception ->
            if (exception != null) {
                tournamentLive.value = Tournament()
                return@addSnapshotListener
            }

            if (document != null) {
                tournamentLive.value = document.toObject<Tournament>()
            }
        }
    }
    private fun loadGameInTourney()
    {
        firestore.collection("Tournaments").document(tournamentId).addSnapshotListener { document, exception ->
            if (exception != null) {
                tournamentLive.value = Tournament()
                return@addSnapshotListener
            }

            if (document != null) {
                val tournament = document.toObject<Tournament>()
                tournamentGamesLive.value = tournament!!.games
            }
        }
    }

}