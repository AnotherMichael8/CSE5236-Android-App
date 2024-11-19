package com.example.cse5236mobileapp.model

import com.google.firebase.firestore.PropertyName
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
    constructor(game: Game) : this(game.teamOne, game.teamTwo, game.teamOneScore, game.teamTwoScore, game.round, game.gamePosition, game.gameStatus)
    companion object {
        const val TAG = "Game Object Class"

        fun getRoundDisplayer(currentRound : Int, numRounds: Int) : String
        {
            return if (currentRound == numRounds) {
                "Final"
            } else if (currentRound == numRounds - 1) {
                "Semifinal"
            } else if (currentRound == numRounds - 2) {
                "Quarterfinal"
            } else {
                "Round $currentRound"
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
            return if (randomInt == 1) {
                teamOne
            } else {
                teamTwo
            }
        }
    }

    fun getRoundName(numRounds: Int): String {
        return if (round == numRounds) {
            "Final"
        } else if (round == numRounds - 1) {
            "Semifinal"
        } else if (round == numRounds - 2) {
            "Quarterfinal"
        } else {
            "Round $round"
        }
    }

    //TODO: Determine whether this can be removed
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
            val game = other
            return this.teamOne == game.teamOne && this.teamTwo == game.teamTwo
                    && this.teamOneScore == game.teamOneScore
                    && this.teamTwoScore == game.teamTwoScore
                    && this.round == game.round
                    && this.gameStatus == game.gameStatus
        }
    }
}