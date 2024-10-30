package com.example.cse5236mobileapp.model.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.TournamentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class TournamentViewModel : ViewModel() {

    private val repository = TournamentRepository()
    private val database = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbUser = user?.email ?: "No email"

    // Tournaments specific for current user
    val userTournamentLive: MutableLiveData<List<Tournament>> by lazy {
        MutableLiveData<List<Tournament>>()
    }

    // Tournaments that are public




    init {
        // Will load the user tournament at the beginning here
        loadUserTournaments()
    }

    private fun loadUserTournaments() {
        // Making variable for list of strings for tournaments assigned to users
        val usersTournaments = mutableSetOf<String>()
        database.collection("Users").document(dbUser).addSnapshotListener { document, exception ->
            if (exception != null) {
                userTournamentLive.value = emptyList()
                return@addSnapshotListener
            }

            if (document != null && document.exists()) {
                usersTournaments.clear()
                usersTournaments.addAll(document.data?.keys ?: emptySet())
                // Will fetch the tournament here

            }

        }
    }

    private fun fetchTourneys(tournamentIds: Set<String>) {
        database.collection("Tournaments").addSnapshotListener { documents, exception ->
            if (exception != null) {
                userTournamentLive.value = emptyList()
                return@addSnapshotListener
            }

            val tournaments = mutableListOf<Tournament>()
            if (documents != null) {
                for (doc in documents) {
                    if (tournamentIds.contains(doc.id)) {
                        val currentTourney = doc.toObject<Tournament>()
                        tournaments.add(currentTourney)
                    }
                }
            }
            userTournamentLive.value = tournaments
        }
    }

    // Calling addTournamentToDatabase from repository
    fun addTournament(tournament: Tournament) {
        repository.addTournamentToDatabase(tournament)
    }


    // Calling deleteTournament from repository
    fun deleteTournament(tournamentId: TournamentIdentifier) {
        repository.deleteTournament(tournamentId)
    }
}

