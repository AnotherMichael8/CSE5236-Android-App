package com.example.cse5236mobileapp.model

import android.util.Log
import com.example.cse5236mobileapp.model.Tournament.Companion.toBoolean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class TournamentRepository {
    private val database = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbUser = user?.email ?: "No email"

    // Method to add tournament to database for user
    fun addTournamentToDatabase(tournament: Tournament) {
        // Generating a random key for the tournament here
        val uuid = UUID.randomUUID().toString()

        // Adding the tournament to the remote firestore
        database.collection("Tournaments").document(uuid).set(tournament)
        val userTournaments = mapOf(uuid to "Today")
        //val userAccount = user?.email ?: "No email"
        database.collection("Users").document(dbUser).set(userTournaments, SetOptions.merge())
    }

    // Method to modify tournament attribute
    fun modifyTournamentAttribute(tournament: TournamentIdentifier, changedPropertyKey: String, newProperty: Any){
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


    // Method to delete tournament from database
    fun deleteTournament(tournament: TournamentIdentifier){
        database.collection("Tournaments").document(tournament.tournamentId).delete(
        ).addOnSuccessListener {
            Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
        }.addOnFailureListener { e->
            Log.d(null, tournament.tournament.tournamentName + " error deleting Tournament: $e")
        }
        database.collection("Users").document(dbUser).update(tournament.tournamentId, FieldValue.delete())
            .addOnSuccessListener {
                Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
            }.addOnFailureListener { e->
                Log.d(null, tournament.tournament.tournamentName + " error deleting Tournament: $e")
            }
        for(gameID in tournament.tournament.gamesIDs)
        {
            Game.deleteGame(gameID)
        }
    }
}