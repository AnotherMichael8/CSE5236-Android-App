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