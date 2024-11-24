package com.example.cse5236mobileapp.model.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.Tournament
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TournamentGamesViewModel(private val tournamentId: String) : ViewModel() {

    private var firestore : FirebaseFirestore? = Firebase.firestore
    private val tournamentViewModel: TournamentViewModel = TournamentViewModel()
    private val tournamentUserViewModel = TournamentUserViewModel()

    private var tournamentListener: ListenerRegistration? = null
    private var gamesListener: ListenerRegistration? = null
    private var userPrivilegesListener: ListenerRegistration? = null

    val tournamentLive: MutableLiveData<Tournament> by lazy {
        MutableLiveData<Tournament>()
    }
    val tournamentGamesLive: MutableLiveData<List<Game>> by lazy {
        MutableLiveData<List<Game>>()
    }
    val currentUserAdminPrivileges: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        firestore = FirebaseFirestore.getInstance()
        loadTournament()
        loadGameInTourney()
        getUserAdminPrivileges()
    }

    private fun loadTournament() {
        tournamentListener = firestore?.collection("Tournaments")?.document(tournamentId)
            ?.addSnapshotListener { document, exception ->
                if (exception != null) {
                    tournamentLive.value = Tournament()
                    return@addSnapshotListener
                }

                if (document != null) {
                    tournamentLive.value = document.toObject<Tournament>()
                }
            }
    }

    private fun loadGameInTourney() {
        gamesListener = firestore?.collection("Tournaments")?.document(tournamentId)
            ?.addSnapshotListener { document, exception ->
                if (exception != null) {
                    tournamentLive.value = Tournament()
                    return@addSnapshotListener
                }

                if (document != null) {
                    val tournament = document.toObject<Tournament>()
                    if (tournament != null)
                        tournamentGamesLive.value = tournament.games
                }
            }
    }

    private fun getUserAdminPrivileges() {
        userPrivilegesListener = firestore?.collection("Users")?.document(tournamentUserViewModel.currentUserEmail()!!)
            ?.addSnapshotListener { document, exception ->
                if (exception != null) {
                    currentUserAdminPrivileges.value = ""
                    return@addSnapshotListener
                }
                if (document != null) {
                    currentUserAdminPrivileges.value = document.getString(tournamentId)
                }
            }
    }

    fun addNewGameToDatabase(game: Game) {
        firestore?.collection("Tournaments")?.document(tournamentId)?.get()
            ?.addOnSuccessListener { document ->
                val tournament = document.toObject<Tournament>()
                tournament?.let {
                    if (!it.games.contains(game)) {  // Add only if game is not already in the list
                        it.games.add(game)
                        tournamentViewModel.updateTournamentGames(it, tournamentId)
                    }
                }
            }
    }

    fun removeGameFromDatabase(game: Game) {
        val team1 = game.teamOne
        val team2 = game.teamTwo
        firestore?.collection("Tournaments")?.document(tournamentId)?.get()
            ?.addOnSuccessListener { document ->
                val tournament = document.toObject<Tournament>()
                tournament?.let {
                    if (it.games.removeMatchUp(team1, team2)) {
                        tournamentViewModel.updateTournamentGames(it, tournamentId)
                    } else {
                        Log.i("GameViewModel", "Matchup doesn't exist and was not removed")
                    }
                }
            }
    }

    fun updateOldGameToNewGameDatabase(gameOld: Pair<String, String>, gameNew: Game) {
        viewModelScope.launch {
            val team1 = gameOld.first
            val team2 = gameOld.second
            val document = firestore?.collection("Tournaments")?.document(tournamentId)?.get()?.await()
            val tournament = document?.toObject<Tournament>()
            tournament?.let {
                //it.games.add(gameNew)
                if (!it.games.removeMatchUp(team1, team2)) {
                    Log.i("GameViewModel", "Matchup doesn't exist and was not removed")
                }
                it.games.add(gameNew.gamePosition, gameNew)
                tournamentViewModel.updateTournamentGames(it, tournamentId)
            }

        }
    }

    private fun MutableList<Game>.removeMatchUp(team1: String, team2: String): Boolean {
        var trueGame: Game?
        trueGame = null
        var isRemoved = false
        for (game in this) {
            if (game.teamOne == team1 && game.teamTwo == team2 || game.teamTwo == team1 && game.teamOne == team2) {
                trueGame = game
            }
        }
        if (trueGame != null) {
            this.remove(trueGame)
            isRemoved = true
        }
        return isRemoved
    }
    fun destroyViewModel()
    {
        tournamentListener?.remove()
        tournamentListener = null
        gamesListener?.remove()
        gamesListener = null
        userPrivilegesListener?.remove()
        userPrivilegesListener = null

        // Clear LiveData
        tournamentLive.value = null
        tournamentGamesLive.value = null
        currentUserAdminPrivileges.value = null

        // Nullify ViewModel fields (if they can be null)
        firestore = null
    }

}