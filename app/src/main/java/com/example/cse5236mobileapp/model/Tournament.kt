package com.example.cse5236mobileapp.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import java.util.UUID
import kotlin.random.Random

data class Tournament (
    @PropertyName("Address") var address: String = "",
    @PropertyName("Date") var date: String = "",
    @PropertyName("EventType") var eventType: String = "",
    @PropertyName("NumberPlayers") var numberPlayers: String = "",
    @PropertyName("Rules") var rules: String = "",
    @PropertyName("Time") var time: String = "",
    @PropertyName("TournamentName") var tournamentName: String = "",
    @PropertyName("isMorning") var isMorning: Boolean = false,
    @PropertyName("isPrivate") var isPrivate: Boolean = false,
    @PropertyName("Players") var players: List<String> = listOf(),
    @PropertyName("Round") var round: Int = 1,
    @PropertyName("Games") var games: MutableList<Game> = mutableListOf<Game>()
)
{
    fun createInitialGames() {
        var gameList = mutableListOf<Game>()
        for(i in 0 until players.size step 2) {
            val game = Game(
                teamOne = players[i],
                teamTwo = players[i + 1],
                round = 1
            )
            games.add(game)
        }
    }

    fun advanceRound() {
        if (games.size > 1) {
            var newGames = mutableListOf<Game>()
            for (i in 0 until games.size step 2) {
                val newTeamOne = getGameWinner(games[i])
                val newTeamTwo = getGameWinner(games[i + 1])
                val newGame = Game(
                    teamOne = newTeamOne,
                    teamTwo = newTeamTwo
                )
                newGames.add(newGame)
            }

            for (newGame in newGames) {
                games.add(newGame)
            }
            round++
        }
    }

    private fun getGameWinner(game: Game): String {
        if (game.teamTwoScore > game.teamOneScore) {
            return game.teamOne
        }
        else if (game.teamOneScore > game.teamTwoScore) {
            return game.teamTwo
        }
        else {
            val randomInt = Random.nextInt(1, 2)
            if (randomInt == 1) {
                return game.teamOne
            }
            else {
                return game.teamTwo
            }
        }
    }

    companion object {
        private const val TAG = "Tournament Class"
        fun getTournamentList(onResult: (ArrayList<TournamentIdentifier>) -> Unit) {
            //This section finds which tournaments the user is in
            val database = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser
            val dbUser = user?.email ?: "No email"

            val usersTournaments = mutableSetOf<String>()
            database.collection("Users").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        if (document.id.equals(dbUser)) {
                            // Retrieve the whole document as a map
                            val data: MutableMap<String, Any> = document.data
                            for (value in data) {
                                usersTournaments.add(value.key)
                                Log.d(TAG, "UUID is ${value.key}")
                            }
                            break
                        }
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
            var tournamentList = arrayListOf<TournamentIdentifier>()

            database.collection("Tournaments").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        if(usersTournaments.contains(document.id)) {
                            Log.d(null, "DocumentSnapshot data: ${document.data}")
                            val tournament = document.toObject<Tournament>()
                            val tournamentId = TournamentIdentifier(document.id, tournament)

                            tournamentList.add(tournamentId)
                        }
                    }
                    onResult(tournamentList)

                } else {
                    Log.d(null, "No such document")
                    onResult(tournamentList)
                }
            }
                .addOnFailureListener { exception ->
                    Log.d(null, "get failed with ", exception)
                    onResult(tournamentList)
                }
        }
        fun addTournamentToDatabase(tournament: Tournament) {
            val user = FirebaseAuth.getInstance().currentUser
            val dbUser = user?.email ?: "No email"

            val database = Firebase.firestore
            val uuid = UUID.randomUUID().toString()
            database.collection("Tournaments").document(uuid).set(tournament)
            val userTournaments = mapOf(uuid to "Today")
            //val userAccount = user?.email ?: "No email"
            database.collection("Users").document(dbUser).set(userTournaments, SetOptions.merge())
        }
        fun modifyTournament(tournament: TournamentIdentifier, changedPropertyKey: String, newProperty: Any){
            val database = Firebase.firestore
            when (changedPropertyKey){
                "Address" -> tournament.tournament.address = newProperty.toString()
                "Date" -> tournament.tournament.date = newProperty.toString()
                "EventType" -> tournament.tournament.eventType = newProperty.toString()
                "NumberPlayers" -> tournament.tournament.numberPlayers = newProperty.toString()
                "Rules" -> tournament.tournament.rules = newProperty.toString()
                "Time" -> tournament.tournament.time = newProperty.toString()
                "TournamentName" -> tournament.tournament.tournamentName = newProperty.toString()
                "isMorning" -> tournament.tournament.isMorning = newProperty.toBoolean()
                "isPrivate" -> tournament.tournament.isPrivate = newProperty.toBoolean()
            }
            database.collection("Tournaments").document(tournament.tournamentId).update(
                changedPropertyKey, when (changedPropertyKey) {
                    "isMorning", "isPrivate" -> newProperty.toBoolean()
                    else -> newProperty.toString()
                }
            ).addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName+ " tournament updated successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName+ " error updating Tournament: $e")
            }
        }


        fun deleteTournament(tournament: TournamentIdentifier){
            val database = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser
            val dbUser = user?.email ?: "No email"

            database.collection("Tournaments").document(tournament.tournamentId).delete(
            ).addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName + " error deleting Tournament: $e")
            }
            database.collection("Users").document(dbUser).update(tournament.tournamentId, FieldValue.delete())
            .addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName + " error deleting Tournament: $e")
            }
        }


        fun Any?.toBoolean(): Boolean {
            return when (this) {
                is Boolean -> this  // Return directly if it's already a Boolean
                is String -> this.lowercase() in listOf("true", "1")  // Convert string representations of true
                else -> false  // Return false for all other cases
            }
        }
    }
}