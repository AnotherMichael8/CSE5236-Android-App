package com.example.cse5236mobileapp.model

import com.google.firebase.firestore.PropertyName
import kotlin.math.ceil
import kotlin.random.Random
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
    @PropertyName("Games") var games: MutableList<Game> = mutableListOf<Game>()
)
{
    fun createInitialGames() {
        var gameList = mutableListOf<Game>()
        for(i in 0 until players.size step 2) {
            val game = Game(
                teamOne = players[i],
                teamTwo = players[i + 1],
                round = 1,
                gamePosition = i / 2
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

    private fun getGameWinner(game: Game): String {
        if (game.teamTwoScore > game.teamOneScore) {
            return game.teamTwo
        }
        else if (game.teamOneScore > game.teamTwoScore) {
            return game.teamOne
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

    fun getNumberOfRounds(): Int {
        return ceil(log2(numberPlayers.toDouble())).toInt()
    }

    // Used to display current Round name for a tournament
    fun roundDisplayer(currentRound: Int): String {
        val roundName = getRoundName(currentRound)
        if (roundName != "Final" && roundName != "Semifinal" && roundName != "Quarterfinal") {
            return "Round ${roundName}"
        }
        else {
            return roundName
        }
    }

    fun getRoundName(currentRound: Int): String {
        val numRounds = getNumberOfRounds()
        if (round == numRounds) {
            return "Final"
        }
        else if (round == numRounds - 1) {
            return "Semifinal"
        }
        else if (round == numRounds - 2) {
            return "Quarterfinal"
        }
        else {
            return round.toString()
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
    }
}