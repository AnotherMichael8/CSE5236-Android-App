package com.example.cse5236mobileapp.model

import com.google.firebase.firestore.PropertyName
import kotlin.math.ceil
import com.google.android.gms.maps.model.LatLng
import kotlin.math.log2

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
    @PropertyName("Games") var games: MutableList<Game> = mutableListOf(),
    @PropertyName("JoinCode") var joinCode: String = "",
    @PropertyName("LatLng") var latLng: LatLng? = null
)
{
    fun createInitialGames() {
        for(i in 0 until numberPlayers.toInt() step 2) {
            val game = Game(
                teamOne = i.toString(),
                teamTwo = (i + 1).toString(),
                round = 1,
                gamePosition = i / 2
            )
            games.add(game)
        }
    }
    //TODO: Determine whether we can delete this
    fun advanceRound() {
        if (games.size > 1) {
            var newGames = mutableListOf<Game>()
            for (i in 0 until games.size step 2) {
                val newTeamOne = games[i].getGameWinner()
                val newTeamTwo = games[i + 1].getGameWinner()
                val newGame = Game(
                    teamOne = newTeamOne,
                    teamTwo = newTeamTwo,
                    round = round + 1
                )
                newGames.add(newGame)
            }

            for (newGame in newGames) {
                games.add(newGame)
            }
            round++
        }
    }


    fun getNumberOfRounds(): Int {
        return ceil(log2(numberPlayers.toDouble())).toInt()
    }

    // TODO: Can probably delete this
    // Used to display current Round name for a tournament
//    fun roundDisplayer(currentRound: Int): String {
//        val roundName = getRoundName(currentRound)
//        return if (roundName != "Final" && roundName != "Semifinal" && roundName != "Quarterfinal") {
//            "Round $roundName"
//        } else {
//            roundName
//        }
//    }
    // TODO: Once function above is deleted then we can delete this too
//    fun getRoundName: String {
//        val numRounds = getNumberOfRounds()
//        return if (round == numRounds) {
//            "Final"
//        } else if (round == numRounds - 1) {
//            "Semifinal"
//        } else if (round == numRounds - 2) {
//            "Quarterfinal"
//        } else {
//            round.toString()
//        }
//    }

    fun generateJoinCode() {
        val possibleValues = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var joinString = ""

        for (i in 1 until 9) {
            joinString += possibleValues.random()
        }

        joinCode = joinString
    }

    fun isTournamentFull(): Boolean {
        return players.size >= numberPlayers.toInt()
    }

    fun isUserAPlayer(email: String?): Boolean {
        return if (email != null) {
            players.contains(email)
        } else {
            // Returns true because can't verify that it isn't for null string
            true
        }
    }

    companion object {
        private const val TAG = "Tournament Class"

        fun Any?.toBoolean(): Boolean {
            return when (this) {
                is Boolean -> this  // Return directly if it's already a Boolean
                is String -> this.lowercase() in listOf("true", "1")  // Convert string representations of true
                else -> false  // Return false for all other cases
            }
        }
        //TODO: Can delete this function
//        fun Any?.toGameList(): MutableList<Game> {
//            if(this !is MutableList<*> || this.isEmpty() || this[0] !is Game)
//            {
//                return mutableListOf()
//            }
//            else{
//                return this as MutableList<Game>
//            }
//        }
    }
}