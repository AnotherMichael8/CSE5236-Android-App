package com.example.cse5236mobileapp.model.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.Tournament
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class TournamentGamesViewModel(private val tournamentId: String) {

    private var firestore = Firebase.firestore
    private val tournamentViewModel : TournamentViewModel = TournamentViewModel()
    private val tournamentUserViewModel = TournamentUserViewModel()

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
                if (tournament != null)
                    tournamentGamesLive.value = tournament.games
            }
        }
    }
    private fun getUserAdminPrivileges()
    {
        firestore.collection("Users").document(tournamentUserViewModel.currentUserEmail()!!).addSnapshotListener { document, exception ->
            if (exception != null) {
                currentUserAdminPrivileges.value = ""
                return@addSnapshotListener
            }
            if(document != null)
            {
                currentUserAdminPrivileges.value = document.getString(tournamentId)
            }
        }
    }

    //TODO: Can be deleted
//    fun addNewGameToDatabase(game: Game) {
//        firestore.collection("Tournaments").document(tournamentId).get().addOnSuccessListener { document ->
//            val tournament = document.toObject<Tournament>()
//            tournament?.let {
//                if (!it.games.contains(game)) {  // Add only if game is not already in the list
//                    it.games.add(game)
//                    tournamentViewModel.updateTournamentGames(it, tournamentId)
//                }
//            }
//        }
//    }
//
//    fun removeGameFromDatabase(game: Game) {
//        val team1 = game.teamOne
//        val team2 = game.teamTwo
//        firestore.collection("Tournaments").document(tournamentId).get().addOnSuccessListener { document ->
//            val tournament = document.toObject<Tournament>()
//            tournament?.let {
//                if(it.games.removeMatchUp(team1, team2))
//                {
//                    tournamentViewModel.updateTournamentGames(it, tournamentId)
//                }
//                else
//                {
//                    Log.i("GameViewModel", "Matchup doesn't exist and was not removed")
//                }
//            }
//        }
//    }
    fun updateOldGameToNewGameDatabase(gameOld: Pair<String, String>, gameNew : Game)
    {
        val team1 = gameOld.first
        val team2 = gameOld.second
        firestore.collection("Tournaments").document(tournamentId).get().addOnSuccessListener { document ->
            val tournament = document.toObject<Tournament>()
            tournament?.let {
                //it.games.add(gameNew)
                if(!it.games.removeMatchUp(team1, team2))
                {
                    Log.i("GameViewModel", "Matchup doesn't exist and was not removed")
                }
                it.games.add(gameNew.gamePosition, gameNew)
                tournamentViewModel.updateTournamentGames(it, tournamentId)
            }
        }
    }
    fun MutableList<Game>.removeMatchUp(team1 : String, team2 : String) : Boolean
    {
        var trueGame : Game?
        trueGame = null
        var isRemoved = false
        for(game in this)
        {
            if(game.teamOne == team1 && game.teamTwo == team2 || game.teamTwo == team1 && game.teamOne == team2)
            {
                trueGame = game
            }
        }
        if(trueGame != null)
        {
            this.remove(trueGame)
            isRemoved = true
        }
        return isRemoved
    }

}