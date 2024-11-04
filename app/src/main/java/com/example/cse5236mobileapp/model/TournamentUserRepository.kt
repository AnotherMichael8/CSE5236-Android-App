package com.example.cse5236mobileapp.model

import android.util.Log
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TournamentUserRepository {
    companion object {
        private const val TAG = "TournamentUserRepository"
    }

    private val database = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()


    // Method to add user to database
    fun addUserToDatabase(email: String, password: String) {
        // Adding the tournament to the remote firestore
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            database.collection("Users").document(email).set(mapOf<String, String>())
            Log.d(TAG, "User: $email successfully added to database")
        }.addOnFailureListener { e ->
            Log.e(TAG, "User failed to be created: $e")
        }
    }

    // Method to modify user email
    fun modifyUser(firebaseUser: FirebaseUser, newEmail: String, newPassword: String){
        if(newEmail != firebaseUser.email && !newPassword.isNullOrBlank() && !newEmail.isNullOrBlank()) {
            //Modify both email and password
            firebaseUser.updatePassword(newPassword).addOnSuccessListener {
                val oldEmail = firebaseUser.email!!

                firebaseUser.updateEmail(newEmail).addOnSuccessListener {
                    // Make new user document with past user data and delete old user document
                    database.collection("Users").document(oldEmail).get()
                        .addOnSuccessListener { document ->
                            val dataSet = document.data?.toMutableMap() ?: mutableMapOf<String, String>()
                            database.collection("Users").document(newEmail).set(dataSet).addOnSuccessListener {
                                database.collection("Users").document(oldEmail).delete()
                                Log.d(TAG, "Successfully created new Users document with $newEmail and deleted $oldEmail")
                            }.addOnFailureListener { e ->
                                Log.e(TAG, "Error creating new Users document with $newEmail: $e")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error fetching document: ${exception.localizedMessage}")
                        }

                    val newUserEmailTemp = firebaseUser.email!!
                    Log.d(TAG, "User email updated successfully: $newUserEmailTemp")
                }.addOnFailureListener { e ->
                    Log.e(TAG, "User email not updated successfully: $e")
                }
                Log.d(TAG, "User password updated successfully")
            }.addOnFailureListener { e ->
                Log.e(TAG, "User password not updated successfully: $e")
            }
        } else if(newEmail != firebaseUser.email && !newEmail.isNullOrBlank()){
            //Modify only email
            val oldEmail = firebaseUser.email!!
            firebaseUser.updateEmail(newEmail).addOnSuccessListener {
                // Make new user document with past user data and delete old user document
                database.collection("Users").document(oldEmail).get()
                    .addOnSuccessListener { document ->
                        val dataSet = document.data?.toMutableMap() ?: mutableMapOf<String, String>()
                        database.collection("Users").document(newEmail).set(dataSet).addOnSuccessListener {
                            database.collection("Users").document(oldEmail).delete()
                            Log.d(TAG, "Successfully created new Users document with $newEmail and deleted $oldEmail")
                        }.addOnFailureListener { e ->
                            Log.e(TAG, "Error creating new Users document with $newEmail: $e")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching document: ${exception.localizedMessage}")
                    }

                val newUserEmailTemp = firebaseUser.email!!
                Log.d(TAG, "User email updated successfully: $newUserEmailTemp")
            }.addOnFailureListener { e ->
                Log.e(TAG, "User email not updated successfully: $e")
            }
        } else if(!newPassword.isNullOrBlank() && newPassword.length >= 6){
            //Modify only password
            firebaseUser.updatePassword(newPassword).addOnSuccessListener {
                Log.d(TAG, "User password updated successfully")
            }.addOnFailureListener { e ->
                Log.e(TAG, "User password failed to update: $e")
            }
        }
    }

    fun modifyUserDisplayName(firebaseUser: FirebaseUser, newDisplayName: String) {
        if (firebaseUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build()

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User display name updated to $newDisplayName successfully.")
                    } else {
                        println("Failed to update display name.")
                    }
                }
        } else {
            println("No user is signed in.")
        }
    }

    // TODO: Determine whether to keep this for simplicity or keep hefty general modify method
    // Method to modify user password
    fun modifyPassword(firebaseUser: FirebaseUser, newPassword: String) {
        firebaseUser.updatePassword(newPassword).addOnSuccessListener {
            Log.d(TAG, "User password updated successfully")
        }.addOnFailureListener { e->
            Log.e(TAG, "User password failed to update: $e")
        }
    }

    // Method to delete user from database
    fun deleteUser(firebaseUser: FirebaseUser, userEmail: String){
        firebaseUser.delete().addOnSuccessListener {
            database.collection("Users").document(userEmail).delete()
            Log.d(TAG, "User account deleted successfully")
        }.addOnFailureListener { e->
            Log.e(TAG, "User account deletion failed: $e")
        }
    }
}