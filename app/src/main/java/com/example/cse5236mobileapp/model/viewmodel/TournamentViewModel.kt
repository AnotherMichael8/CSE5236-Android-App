package com.example.cse5236mobileapp.model.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.TournamentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.rpc.Code

class TournamentViewModel : ViewModel() {

    private val repository = TournamentRepository()
    private var firestore = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbUser = user?.email ?: "No email"

    // Tournaments specific for current user
    val userTournamentLive: MutableLiveData<List<TournamentIdentifier>> by lazy {
        MutableLiveData<List<TournamentIdentifier>>()
    }


    init {
        // Will load the user tournament at the beginning here
        loadUserTournaments()
        firestore = FirebaseFirestore.getInstance()
    }

    private fun loadUserTournaments() {
        Log.d(null, "Calls loadusertourney")
        // Making variable for list of strings for tournaments assigned to users
        val usersTournaments = mutableSetOf<String>()
        firestore.collection("Users").document(dbUser).addSnapshotListener { document, exception ->
            if (exception != null) {
                userTournamentLive.value = emptyList()
                Log.w(TAG, "Listen Failed", exception)
                return@addSnapshotListener
            }

            if (document != null && document.exists()) {
                usersTournaments.clear()
                usersTournaments.addAll(document.data?.keys ?: emptySet())
                // Will fetch the tournament here
                fetchTourneys(usersTournaments)

            }

        }
    }

    private fun fetchTourneys(tournamentIds: Set<String>) {
        firestore.collection("Tournaments").addSnapshotListener { documents, exception ->
            if (exception != null) {
                userTournamentLive.value = emptyList()
                return@addSnapshotListener
            }

            val tournaments = mutableListOf<TournamentIdentifier>()
            if (documents != null) {
                for (doc in documents) {
                    if (tournamentIds.contains(doc.id)) {
                        val currentTourney = doc.toObject<Tournament>()
                        tournaments.add(TournamentIdentifier(doc.id, currentTourney))
                    }
                }
            }
            userTournamentLive.value = tournaments
        }
    }

    fun findTourneyIdFromJoinCode(joinCode: String, onResult: (TournamentIdentifier?) -> Unit) {
        val tourneys = firestore.collection("Tournaments").whereEqualTo("joinCode", joinCode)
            .get()
            .addOnSuccessListener { tournaments ->
                if (tournaments.size() > 0) {
                    val firstInstance = tournaments.documents[0]
                    val tournamentConverted = firstInstance.toObject<Tournament>()
                    if (tournamentConverted != null) {
                        onResult(TournamentIdentifier(firstInstance.id, tournamentConverted))
                    } else {
                        onResult(null)
                        Log.e(TAG, "Failure to retrieve tournament, error converting object.")
                    }
                }
                else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failure to retrieve tournament.", exception)
                onResult(null)
            }
    }

    // Calling addTournamentToDatabase from repository
    fun addTournament(tournament: Tournament): String {
        return repository.addTournamentToDatabase(tournament)
    }

    // Calling modifyTournamentAttribute from repository
    fun modifyTournamentAttribute(
        tournament: TournamentIdentifier,
        changedPropertyKey: String,
        newProperty: Any
    ) {
        repository.modifyTournamentAttribute(tournament, changedPropertyKey, newProperty)
    }

    fun updateTournamentGames(tournament: Tournament, tournamentID: String) {
        repository.updateGamesAndRounds(tournament, tournamentID)
    }

    // Calling deleteTournament from repository
    fun deleteTournament(tournamentId: TournamentIdentifier) {
        repository.deleteTournament(tournamentId)
    }
}

