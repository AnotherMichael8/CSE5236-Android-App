package com.example.cse5236mobileapp.model

import android.util.Log
import android.widget.Toast
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
    fun addUserToDatabase(email: String, password: String, username: String, onComplete: (Boolean) -> Unit) {
        // Adding the tournament to the remote firestore
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {

            modifyUserDisplayName(auth.currentUser!!, username) {success ->
                if (success) {

                    database.collection("Users").document(email)
                        .set(mapOf<String, String>())
                    database.collection("Users").document(email)
                        .set(mapOf("username" to username))
                    Log.d(TAG, "User: $email successfully added to database")
                    onComplete(true)
                }
                else {
                    // TODO: Delete the current user as it is a failure
                    auth.currentUser!!.delete().addOnSuccessListener {
                        onComplete(false)
                    }
                        .addOnFailureListener {
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



    fun modifyUserDisplayName(firebaseUser: FirebaseUser, newDisplayName: String, onComplete: (Boolean) -> Unit) {
        if (firebaseUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build()

            firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User display name updated to $newDisplayName successfully.")
                        onComplete(true)
                    } else {
                        println("Failed to update display name.")
                        onComplete(false)
                    }
                }
        } else {
            println("No user is signed in.")
            onComplete(false)
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

    fun modifyDisplayName(firebaseUser: FirebaseUser, newDisplayName: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }
        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Update database here
                    firebaseUser.email?.let {
                        database.collection("Users").document(it)
                            .update("username", newDisplayName)
                            .addOnSuccessListener {Log.d(TAG, "Successfully changed username in database") }
                            .addOnFailureListener {Log.w(TAG, "Failed to update database accordingly")}
                    }
                }
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