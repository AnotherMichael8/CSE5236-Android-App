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
    @PropertyName("Round") var round: Int = 1
)
{
    companion object {
        const val TAG = "Game Object Class"
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
}