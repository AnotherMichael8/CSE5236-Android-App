package com.example.cse5236mobileapp.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TournamentUserRepository {
    companion object {
        private const val TAG = "TournamentUserRepository"
    }

    private val database = Firebase.firestore


    // Method to add user to database
    fun addUserToDatabase(user: TournamentUser, password: String) {
        // Adding the tournament to the remote firestore
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.userEmail, password).addOnSuccessListener {
            database.collection("Users").document(user.userEmail).set(mapOf<String, String>())
            Log.d(TAG, "User: ${user.userEmail} successfully added to database")
        }.addOnFailureListener { e ->
            Log.e(TAG, "User failed to be created: $e")
        }
    }

    // Method to modify user email
    fun modifyEmail(firebaseUser: FirebaseUser, user: TournamentUser, newEmail: String){
        if(newEmail != firebaseUser.email) {
            firebaseUser.updateEmail(newEmail).addOnSuccessListener {
                // TODO: If there are any references to user emails within tournaments or games then they would also need to be updated here also
                // Make new user document with past user data and delete old user document
                database.collection("Users").document(newEmail).set(database.collection("Users").document(user.userEmail).get())
                database.collection("Users").document(user.userEmail).delete()
                user.userEmail = newEmail
                Log.d(TAG, "User email updated successfully")
            }.addOnFailureListener { e ->
                Log.e(TAG, "User email not updated successfully: $e")
            }
        } else {
            Log.d(TAG, "User email not updated successfully: new user email matches original")
        }
    }

    // Method to modify user password
    fun modifyPassword(firebaseUser: FirebaseUser, newPassword: String) {
        firebaseUser.updatePassword(newPassword).addOnSuccessListener {
            Log.d(TAG, "User password updated successfully")
        }.addOnFailureListener { e->
            Log.e(TAG, "User password failed to update: $e")
        }
    }

    // Method to delete user from database
    fun deleteUser(firebaseUser: FirebaseUser, user: TournamentUser){
        firebaseUser.delete().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                database.collection("Users").document(user.userEmail).delete()
                Log.d(TAG, "User account deleted successfully")
            } else {
                Log.e(TAG, "User account deletion failed")
            }
        }
    }
}