package com.example.cse5236mobileapp.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TournamentUserRepository {
    companion object {
        private const val TAG = "TournamentUserRepository"
    }

    private val database = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser?.email ?: "No email"


    // Method to add user to database
    fun addUserToDatabase(
        email: String, password: String, username: String, onComplete: (Boolean) -> Unit
    ) {
        // Adding the tournament to the remote firestore
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                modifyUserDisplayName(auth.currentUser!!, username) { success ->
                    if (success) {

                        database.collection("Users").document(email).set(mapOf<String, String>())
                        database.collection("Users").document(email)
                            .set(mapOf("username" to username))
                        Log.d(TAG, "User: $email successfully added to database")
                        onComplete(true)
                    } else {
                        // TODO: Delete the current user as it is a failure
                        auth.currentUser!!.delete().addOnSuccessListener {
                            onComplete(false)
                        }.addOnFailureListener {
                            onComplete(true)
                        }
                        onComplete(false)
                    }
                }


            }.addOnFailureListener { e ->
                Log.e(TAG, "User failed to be created: $e")
                onComplete(false)
            }
    }


    // can stay public because it makes sense for us to use this outside of the class itself
    fun modifyUserDisplayName(
        firebaseUser: FirebaseUser, newDisplayName: String, onComplete: (Boolean) -> Unit
    ) {
        val profileUpdates =
            UserProfileChangeRequest.Builder().setDisplayName(newDisplayName).build()

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("User display name updated to $newDisplayName successfully.")
                onComplete(true)
            } else {
                println("Failed to update display name.")
                onComplete(false)
            }
        }

    }

    // TODO: Determine whether to keep this for simplicity or keep hefty general modify method
    // Method to modify user password
    fun modifyPassword(firebaseUser: FirebaseUser, newPassword: String) {
        firebaseUser.updatePassword(newPassword).addOnSuccessListener {
            Log.d(TAG, "User password updated successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "User password failed to update: $e")
        }
    }

    suspend fun modifyDisplayName(firebaseUser: FirebaseUser, newDisplayName: String) {
        withContext(Dispatchers.IO) {
            val profileUpdates = userProfileChangeRequest {
                displayName = newDisplayName
            }
            try {
                firebaseUser.updateProfile(profileUpdates).await()
                firebaseUser.email?.let {
                    database.collection("Users").document(it).update("username", newDisplayName)
                        .await()
                    Log.d(TAG, "Successfully changed username to $newDisplayName")
                }
            } catch (e: Exception) {
                Log.w(
                    TAG, "Failed to update username to $newDisplayName"
                )
            }

        }
    }


    // Method to delete user from database
    suspend fun deleteUser(
        firebaseUser: FirebaseUser, userEmail: String, onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                firebaseUser.delete().await()
                database.collection("Users").document(userEmail).delete()
                Log.d(TAG, "$userEmail account deleted successfully")
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Failure to delete $userEmail.", e)
                onComplete(false)
            }
        }
    }

    // Add tournaments for users
    suspend fun addTournamentForUsers(tournamentID: String, playerEmailList: List<String>) {
        withContext(Dispatchers.IO) {
            // First focus on adding the admin to db first
            val tournamentToAdmin = mapOf(tournamentID to "Admin")

            try {
                //val userAccount = user?.email ?: "No email"
                database.collection("Users").document(user)
                    .set(tournamentToAdmin, SetOptions.merge()).await()

                Log.d(TAG, "Added tournament $tournamentID for admin $user.")
            } catch (e: Exception) {
                Log.e(TAG, "Failure to add tournament $tournamentID for admin $user.")
            }

            // Now work on linking with the rest of users
            for (player in playerEmailList) {
                if (player != user) {
                    val tournamentToUser = mapOf(tournamentID to "User")
                    try {
                        database.collection("Users").document(player)
                            .set(tournamentToUser, SetOptions.merge()).await()
                        Log.d(TAG, "Added tournament $tournamentID for user $player.")
                    } catch (e: Exception) {
                        Log.e(
                            TAG, "Failure to add tournament $tournamentID for user $player.", e
                        )
                    }
                }
            }
        }
    }

    suspend fun addTournamentForUser(tournamentID: String) {
        withContext(Dispatchers.IO) {
            val tournamentToUser = mapOf(tournamentID to "User")
            try {
                database.collection("Users").document(user)
                    .set(tournamentToUser, SetOptions.merge())
                Log.d(TAG, "Added $user to tournament $tournamentID.")
            } catch (e: Exception) {
                Log.e(TAG, "Failure to add $user to tournament $tournamentID.")
            }
        }
    }


    // Remove tournament for all users
    suspend fun removeTournamentForAllUsers(
        tournamentID: String, playerEmailList: List<String>
    ) {
        withContext(Dispatchers.IO) {
            // Looping through each player on the email list
            for (playerEmail in playerEmailList) {
                // Removal from database
                val ref = database.collection("Users").document(playerEmail)
                try {
                    ref.update(tournamentID, FieldValue.delete()).await()

                    Log.d(TAG, "Successfully remove $tournamentID for $playerEmail")
                } catch (e: Exception) {
                    Log.e(TAG, "Failure to remove $tournamentID for $playerEmail", e)

                }
            }
        }
    }
}