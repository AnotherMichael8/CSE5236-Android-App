package com.example.cse5236mobileapp

import android.util.Log
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class Game (
    @PropertyName("TeamOne") var teamOne: String = "",
    @PropertyName("TeamTwo") var teamTwo: String = "",
    @PropertyName("TeamOneScore") var teamOneScore: Int = 0,
    @PropertyName("TeamTwoScore") var teamTwoScore: Int = 0
)
{
    companion object {
        fun createGame(game: Game) {
            val database = Firebase.firestore

            val uuid = "Game" + UUID.randomUUID().toString()
            database.collection("Games").document(uuid).set(game).addOnSuccessListener { document ->
                Log.d(null, "Game created with id: $uuid")
            }.addOnFailureListener {
                Log.w(null, "Game unable to create")
            }
        }

        fun deleteGame(gameId: String) {
            val database = Firebase.firestore

            database.collection("Games").document(gameId).delete().addOnSuccessListener { doc ->
                Log.d(null, "Game deleted with id: $gameId")
            }.addOnFailureListener {
                Log.w(null, "Game not deleted")
            }
        }
    }
}