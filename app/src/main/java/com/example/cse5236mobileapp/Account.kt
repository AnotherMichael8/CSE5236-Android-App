package com.example.cse5236mobileapp

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth

class Account {
    companion object {
        fun checkCreateUserInfo(context: Context, username: String, password: String, reentered: String): Boolean {
            // Checking the three fields to see if they're empty
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(context, "Missing Username", Toast.LENGTH_SHORT).show()
                return false
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Missing Password", Toast.LENGTH_SHORT).show()
                return false
            }
            if (TextUtils.isEmpty(reentered)) {
                Toast.makeText(context, "Please Reenter Password", Toast.LENGTH_SHORT).show()
                return false
            }

            // See if passwords match
            if(!TextUtils.equals(password,reentered)) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

        fun checkLoginInputs (context: Context, username: String, password: String): Boolean {
            // Checking the three fields to see if they're empty
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(context, "Missing Username", Toast.LENGTH_SHORT).show()
                return false
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Missing Password", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

        fun login(auth: FirebaseAuth, context: Context, username: String, password: String, onResult: (Boolean) -> Unit) {
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(null, "signInWithEmail:success")
                        onResult(true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(null, "signInWithEmail:failure", task.exception)
                        onResult(false)
                    }
                }
        }

        fun createAccount(auth: FirebaseAuth, context: Context, username: String, password: String, onResult: (Boolean) -> Unit) {
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(null, "createUserWithEmail:success")
                        onResult(true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(null, "createUserWithEmail:failure", task.exception)
                        onResult(false)
                    }
                }
        }
    }
}