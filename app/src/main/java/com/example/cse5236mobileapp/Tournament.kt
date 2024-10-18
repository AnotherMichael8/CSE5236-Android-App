package com.example.cse5236mobileapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

data class Tournament (
    @PropertyName("Address") val address: String = "",
    @PropertyName("Date") val date: String = "",
    @PropertyName("EventType") val eventType: String = "",
    @PropertyName("NumberPlayers") val numberPlayers: String = "",
    @PropertyName("Rules") val rules: String = "",
    @PropertyName("Time") val time: String = "",
    @PropertyName("TournamentName") val tournamentName: String = "",
    @PropertyName("isMorning") val isMorning: Boolean = false,
    @PropertyName("isPrivate") val isPrivate: Boolean = false
)
{
    companion object {
        fun getTournamentList(onResult: (ArrayList<TournamentIdentifier>) -> Unit) {
            val database = Firebase.firestore
            var tournamentList = arrayListOf<TournamentIdentifier>()

            database.collection("Tournaments").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        Log.d(null, "DocumentSnapshot data: ${document.data}")
                        val tournament = document.toObject<Tournament>()
                        val tournamentId = TournamentIdentifier(document.id, tournament)

                        tournamentList.add(tournamentId)
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
    }
}