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
    @PropertyName("TeamTwoScore") var teamTwoScore: Int = 0
)
{
    companion object {
        fun createGame(game: Game) : String {
            val database = Firebase.firestore

            val uuid = "Game" + UUID.randomUUID().toString()
            database.collection("Games").document(uuid).set(game).addOnSuccessListener { document ->
                Log.d(null, "Game created with id: $uuid")
                //onResult(uuid)
            }.addOnFailureListener {
                Log.w(null, "Game unable to create")
                //onResult(null)
            }
            return uuid
        }

        fun deleteGame(gameId: String) {
            val database = Firebase.firestore

            database.collection("Games").document(gameId).delete().addOnSuccessListener { doc ->
                Log.d(null, "Game deleted with id: $gameId")
            }.addOnFailureListener {
                Log.w(null, "Game not deleted")
            }
        }

        fun editPlayerScore(gameId: String, newScore: Int, isTeamOne: Boolean) {
            val database = Firebase.firestore

            val attributeName = if (isTeamOne) "teamOneScore" else "teamTwoScore"

            database.collection("Games").document(gameId).update(attributeName, newScore).addOnSuccessListener {
                Log.d(null, "Changed $attributeName to $newScore")
            }.addOnFailureListener {
                Log.w(null, "Failure to change score")
            }
        }

        fun editPlayerName(gameId: String, newName: String, isTeamOne: Boolean) {
            val database = Firebase.firestore

            val attributeName = if (isTeamOne) "teamOne" else "teamTwo"

            database.collection("Games").document(gameId).update(attributeName, newName).addOnSuccessListener {
                Log.d(null, "Changed $attributeName to $newName")
            }.addOnFailureListener {
                Log.w(null, "Failure to change score")
            }
        }
    }
}