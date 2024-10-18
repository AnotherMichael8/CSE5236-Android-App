package com.example.cse5236mobileapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

data class Tournament (
    @PropertyName("Address") var address: String = "",
    @PropertyName("Date") var date: String = "",
    @PropertyName("EventType") var eventType: String = "",
    @PropertyName("NumberPlayers") var numberPlayers: String = "",
    @PropertyName("Rules") var rules: String = "",
    @PropertyName("Time") var time: String = "",
    @PropertyName("TournamentName") var tournamentName: String = "",
    @PropertyName("isMorning") var isMorning: Boolean = false,
    @PropertyName("isPrivate") var isPrivate: Boolean = false
)
{
    companion object {
        private const val TAG = "Tournament Class"

        fun getTournamentList(onResult: (ArrayList<TournamentIdentifier>) -> Unit) {
            val database = Firebase.firestore
            val user = FirebaseAuth.getInstance().currentUser
            val dbUser = user?.email ?: "No email"

            //This section finds which tournaments the user is in
            val usersTournaments = mutableSetOf<String>()
            database.collection("Users").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        if (document.id.equals(dbUser)) {
                            // Retrieve the whole document as a map
                            val data: MutableMap<String, Any> = document.data
                            for (value in data) {
                                usersTournaments.add(value.key)
                                Log.d(TAG, "UUID is ${value.key}")
                            }
                            break
                        }
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
            var tournamentList = arrayListOf<TournamentIdentifier>()

            database.collection("Tournaments").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {
                        if(usersTournaments.contains(document.id)) {
                            Log.d(null, "DocumentSnapshot data: ${document.data}")
                            val tournament = document.toObject<Tournament>()
                            val tournamentId = TournamentIdentifier(document.id, tournament)

                            tournamentList.add(tournamentId)
                        }
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
        fun modifyTournament(tournament: TournamentIdentifier, changedPropertyKey: String, newProperty: Any){
            val database = Firebase.firestore
            when (changedPropertyKey){
                "Address" -> tournament.tournament.address = newProperty.toString()
                "Date" -> tournament.tournament.date = newProperty.toString()
                "EventType" -> tournament.tournament.eventType = newProperty.toString()
                "NumberPlayers" -> tournament.tournament.numberPlayers = newProperty.toString()
                "Rules" -> tournament.tournament.rules = newProperty.toString()
                "Time" -> tournament.tournament.time = newProperty.toString()
                "TournamentName" -> tournament.tournament.tournamentName = newProperty.toString()
                "isMorning" -> tournament.tournament.isMorning = newProperty.toBoolean()
                "isPrivate" -> tournament.tournament.isPrivate = newProperty.toBoolean()
            }
            database.collection("Tournaments").document(tournament.tournamentId).update(
                changedPropertyKey, when (changedPropertyKey) {
                    "isMorning", "isPrivate" -> newProperty.toBoolean()
                    else -> newProperty.toString()
                }
            ).addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName+ " tournament updated successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName+ " error updating Tournament: $e")
            }
        }
        fun deleteTournament(tournament: TournamentIdentifier){
            val database = Firebase.firestore
            database.collection("Tournaments").document(tournament.tournamentId).delete(
            ).addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName + " error deleting Tournament: $e")
            }
        }
        fun Any?.toBoolean(): Boolean {
            return when (this) {
                is Boolean -> this  // Return directly if it's already a Boolean
                is String -> this.lowercase() in listOf("true", "1")  // Convert string representations of true
                else -> false  // Return false for all other cases
            }
        }
    }
}