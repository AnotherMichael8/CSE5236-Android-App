package com.example.cse5236mobileapp.model

import android.util.Log
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class Game (
    @PropertyName("TeamOne") var teamOne: String = "",
    @PropertyName("TeamTwo") var teamTwo: String = "",
    @PropertyName("TeamOneScore") var teamOneScore: Int = 0,
    @PropertyName("TeamTwoScore") var teamTwoScore: Int = 0,
    @PropertyName("Round") var round: Int = 1
)
{
    companion object {
        const val TAG = "Game object class"
    }

    fun roundDisplayer(numRounds: Int): String {
        val roundName = getRoundName(numRounds)
        if (roundName != "Final" && roundName != "Semifinal" && roundName != "Quarterfinal") {
            return "Round ${roundName}"
        }
        else {
            return roundName
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
            return round.toString()
        }
    }
}