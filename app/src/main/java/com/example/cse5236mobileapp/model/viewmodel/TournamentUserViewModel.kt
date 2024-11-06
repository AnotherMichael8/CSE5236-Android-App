package com.example.cse5236mobileapp.model.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.TournamentUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TournamentUserViewModel : ViewModel() {

    companion object {
        private const val TAG = "TournamentUserViewModel"
    }

    private var firestore = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val repository = TournamentUserRepository()

    val allUsersInfoLive: MutableLiveData<List<TournamentUser>> by lazy {
        MutableLiveData<List<TournamentUser>>()
    }

    val usernameLive: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //Might not be needed yet... NOTE: still WIP

    init {
        val users = mutableSetOf<TournamentUser>()
        firestore.collection("Users").addSnapshotListener { query, exception ->
            if (exception != null) {
                allUsersInfoLive.value = emptyList()
                Log.w(TAG, "Listen Failed", exception)
                return@addSnapshotListener
            }

            if (query != null) {
                users.clear()
                for (document in query.documents) {
                    val dataMap = document.data?.mapNotNull { (key, value) ->
                        if (value is String) key to value else null
                    }?.toMap()?.toMutableMap() ?: mutableMapOf()

                    val tournamentUserTemp = document.getString("username")
                        ?.let { TournamentUser(it,document.id, dataMap) }
                    if (tournamentUserTemp != null) {
                        users.add(tournamentUserTemp)
                    }
//                    TournamentUser(document.id, (document.data?.keys?.toMutableList() ?: mutableListOf<String>(), ))
//                    users.add(TournamentUser(document.id, document.data?.keys?.toMutableList() ?: mutableListOf<String>()))
                }
                allUsersInfoLive.value = users.toList()
            }
        }
        userNameRetrieve()
        firestore = FirebaseFirestore.getInstance()
    }

    // Get the usernameLive
    private fun userNameRetrieve() {
        user?.email?.let {
            firestore.collection("Users").document(it).addSnapshotListener { userObject, exception ->
                if (exception != null) {
                    Log.e(TAG, "Error retrieving username", exception)
                    return@addSnapshotListener
                } else if (userObject != null && userObject.exists()) {
                    // TODO: Cast to object to do this
                    usernameLive.value = userObject.getString("username").toString()
                } else {
                    usernameLive.value = ""
                }
            }
        }
    }


    // Calling addUserToDatabase from repository
    fun addUser(email: String, password: String, username: String, onComplete: (Boolean) -> Unit) {
        repository.addUserToDatabase(email, password, username) { success ->
            if (success) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    fun modifyUser(firebaseUser: FirebaseUser, newDisplayName: String, newPassword: String) {
        if (newDisplayName != "") {
            repository.modifyDisplayName(firebaseUser, newDisplayName)
        }
        if (newPassword != "") {
            repository.modifyPassword(firebaseUser, newPassword)
        }
    }
//
//    // Calling modifyEmail from repository
//    fun modifyUserEmail(firebaseUser: FirebaseUser, oldEmail: String, newEmail: String) {
//        repository.modifyEmail(firebaseUser, oldEmail, newEmail)
//    }
//
//    // Calling modifyPassword from repository
//    fun modifyUserPassword(firebaseUser: FirebaseUser, newPassword: String) {
//        repository.modifyPassword(firebaseUser, newPassword)
//    }

    // Calling deleteUser from repository
    fun deleteUser(firebaseUser: FirebaseUser, userEmail: String) {
        repository.deleteUser(firebaseUser, userEmail)
    }

}