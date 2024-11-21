package com.example.cse5236mobileapp.model

import android.util.Log
import com.example.cse5236mobileapp.model.Tournament.Companion.toBoolean
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class TournamentRepository {
    companion object {
        private const val TAG = "TournamentRepository"
    }

    private val database = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbUser = user?.email ?: "No email"

    // Method to add tournament to database for user
    suspend fun addTournamentToDatabase(tournament: Tournament): String {
            // Generating a random key for the tournament here
            val uuid = UUID.randomUUID().toString()

        withContext(Dispatchers.IO) {
            try {
                // Adding the tournament to the remote firestore
                database.collection("Tournaments").document(uuid).set(tournament).await()

                val userTournaments = mapOf(uuid to "Admin")
                //val userAccount = user?.email ?: "No email"
                database.collection("Users").document(dbUser)
                    .set(userTournaments, SetOptions.merge()).await()
                Log.d(TAG, "Successfully added tournament, $uuid, to database.")
            }
            catch (e: Exception) {
                Log.e(TAG, "Failure to add tournament, $uuid, to database.")
            }
        }
            // Calling method here to add users to tournamentViewModel
            return uuid
    }

    // Method to modify tournament attribute
    suspend fun modifyTournamentAttribute(
        tournament: TournamentIdentifier, changedPropertyKey: String, newProperty: Any
    ) {
        withContext(Dispatchers.IO) {
            when (changedPropertyKey) {
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

            try {
                database.collection("Tournaments").document(tournament.tournamentId).update(
                    changedPropertyKey, when (changedPropertyKey) {
                        "isMorning", "isPrivate" -> newProperty.toBoolean()
                        else -> newProperty.toString()
                    }
                ).await()
                Log.d(
                    TAG, tournament.tournament.tournamentName + " tournament updated successfully"
                )
            } catch (e: Exception) {
                Log.e(TAG, tournament.tournament.tournamentName + " error updating Tournament.", e)
            }

        }
    }

// Used for advancing the tournament
suspend fun updateGamesAndRounds(tournament: Tournament, tournamentId: String) {
    withContext(Dispatchers.IO) {
        val dbRef = database.collection("Tournaments").document(tournamentId)

        try {
            dbRef.update("games", tournament.games).await()
            dbRef.update("round", tournament.round).await()

            Log.d(TAG, "Successfully updated tournament games")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update tournament games", e)
        }
    }
}


// Method to delete tournament from database
suspend fun deleteTournament(tournament: TournamentIdentifier) {
    withContext(Dispatchers.IO) {
        try {
            database.collection("Tournaments").document(tournament.tournamentId).delete().await()
            database.collection("Users").document(dbUser)
                .update(tournament.tournamentId, FieldValue.delete()).await()
            Log.d(null, tournament.tournament.tournamentName + " deleted successfully")
        } catch (e: Exception) {
            Log.e(
                null, tournament.tournament.tournamentName + " error deleting Tournament: $e", e
            )
        }
    }
}


suspend fun addUserToTournament(tournamentId: String, previousPlayers: List<String>) {
    withContext(Dispatchers.IO) {
        val newPlayerList = previousPlayers.toMutableList()
        newPlayerList.add(dbUser)

        val updatedPlayers = hashMapOf("players" to newPlayerList)

        try {
            database.collection("Tournaments").document(tournamentId)
                .set(updatedPlayers, SetOptions.merge()).await()
            Log.d(TAG, "$dbUser added to $tournamentId successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Failure to add $dbUser to $tournamentId successfully.", e)
        }
    }
}
}
