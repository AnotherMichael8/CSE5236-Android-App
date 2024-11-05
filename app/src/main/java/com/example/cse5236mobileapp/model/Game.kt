package com.example.cse5236mobileapp.model

import android.util.Log
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID
import kotlin.random.Random

class Game (
    @PropertyName("TeamOne") var teamOne: String = "",
    @PropertyName("TeamTwo") var teamTwo: String = "",
    @PropertyName("TeamOneScore") var teamOneScore: Int = 0,
    @PropertyName("TeamTwoScore") var teamTwoScore: Int = 0,
    @PropertyName("Round") var round: Int = 1,
    @PropertyName("GamePosition") var gamePosition: Int = 0,
    @PropertyName("GameStatus") var gameStatus: String = "In Progress"
)
{
    companion object {
        const val TAG = "Game Object Class"

        fun getRoundDisplayer(currentRound : Int, numRounds: Int) : String
        {
            if (currentRound == numRounds) {
                return "Final"
            }
            else if (currentRound == numRounds - 1) {
                return "Semifinal"
            }
            else if (currentRound == numRounds - 2) {
                return "Quarterfinal"
            }
            else {
                return "Round $currentRound"
            }
        }
    }

    fun getGameWinner(): String {
        if (teamTwoScore > teamOneScore) {
            return teamTwo
        }
        else if (teamOneScore > teamTwoScore) {
            return teamOne
        }
        else {
            val randomInt = Random.nextInt(1, 2)
            if (randomInt == 1) {
                return teamOne
            }
            else {
                return teamTwo
            }
        }
    }

    fun getRoundName(numRounds: Int): String {
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
            return "Round $round"
        }
    }
    fun MutableList<Game>.getMatchUp(team1 : String, team2 : String) : Game?
    {
        for(game in this)
        {
            if(game.teamOne == team1 && game.teamTwo == team2 || game.teamTwo == team1 && game.teamOne == team2)
            {
                return game
            }
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Game)
        {
            return false
        }
        else
        {
            val game = other as Game
            return this.teamOne == game.teamOne && this.teamTwo == game.teamTwo
                    && this.teamOneScore == game.teamOneScore
                    && this.teamTwoScore == game.teamTwoScore
                    && this.round == game.round
                    && this.gameStatus == game.gameStatus
        }
    }
}