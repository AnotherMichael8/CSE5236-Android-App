package com.example.cse5236mobileapp.model.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TournamentUsernameViewModel(private var emailList: List<String>): ViewModel() {
    companion object {
        private const val TAG = "TournamentUsernameViewModel"
    }

    private  var firestore = Firebase.firestore

    val emailToUsername: MutableLiveData<Map<String, String>> by lazy {
        MutableLiveData<Map<String, String>>()
    }

    init {
        fetchRelevantUserNames()
    }

    private fun fetchRelevantUserNames() {
        viewModelScope.launch(Dispatchers.IO) {
            // Map storage linking emails and usernames
            val emailUserMap = mutableMapOf<String, String>()
            // List of completed emails
            val completed = mutableListOf<String>()

            // Looping through all emails, asynchronously
            for (email in emailList) {
                // Querying username for each
                firestore.collection("Users").document(email)
                    .addSnapshotListener { document, exception ->
                        if (exception != null) {
                            emailUserMap[email] = email
                            completed.add(email)
                            Log.e(TAG, "Failure to find username for $email", exception)
                            if (completed.size == emailList.size) {
                                emailToUsername.value = emailUserMap
                            }
                            return@addSnapshotListener
                        } else if (document != null && document.exists()) {
                            emailUserMap[email] = document.getString("username") ?: email
                            completed.add(email)
                        } else {
                            emailUserMap[email] = email
                            completed.add(email)
                        }

                        // If all added, then update live data
                        if (completed.size == emailList.size)
                            emailToUsername.value = emailUserMap
                    }
            }
        }
    }

    fun updateEmailList(newEmailList: List<String>) {
        emailList = newEmailList
        fetchRelevantUserNames()
    }
}