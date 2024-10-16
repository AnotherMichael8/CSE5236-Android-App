package com.example.cse5236mobileapp

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

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
    }
}