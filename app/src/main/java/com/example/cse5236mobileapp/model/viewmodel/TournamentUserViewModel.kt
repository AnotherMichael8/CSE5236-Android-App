package com.example.cse5236mobileapp.model.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.TournamentUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

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

    private val emailListLive: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
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
                        ?.let { TournamentUser(it, document.id, dataMap) }
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
        emailListRetrieve()
        firestore = FirebaseFirestore.getInstance()
    }

    // Get the usernameLive
    private fun userNameRetrieve() {
        user?.email?.let {
            firestore.collection("Users").document(it)
                .addSnapshotListener { userObject, exception ->
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

    private fun emailListRetrieve() {
        emailListLive.value = mutableListOf()
        firestore.collection("Users").addSnapshotListener { users, exception ->
            if (exception != null) {
                Log.e(TAG, "Error retrieving emails", exception)
                return@addSnapshotListener
            }
            else if (users != null) {
                emailListLive.value = users.map { it.id }
            }
        }
    }

    fun currentUserEmail(): String? {
        return user?.email
    }

    // Verify a list of emails for the viewModel
    fun verifyEmailList(emails: List<String>): Boolean {
        var isVerified = true
        val emailList = emailListLive.value

        if (emailList != null) {
            for (email in emails) {
                if (!emailList.contains(email)) {
                    isVerified = false
                }
            }
        }
        else {
            isVerified = false
        }
        return isVerified
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
        viewModelScope.launch {
            if (newDisplayName != "") {
                repository.modifyDisplayName(firebaseUser, newDisplayName)
            }
            if (newPassword != "") {
                repository.modifyPassword(firebaseUser, newPassword)
            }
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
    fun deleteUser(firebaseUser: FirebaseUser, userEmail: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.deleteUser(firebaseUser, userEmail) { result ->
                if (result) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
        }
    }

    fun addTournamentForUser(tournamentID: String, playerEmailList: List<String>) {
        viewModelScope.launch {
            repository.addTournamentForUsers(tournamentID, playerEmailList)
        }
    }

    fun removeTournamentForAllUsers(tournamentID: String, playerEmailList: List<String>) {
        viewModelScope.launch {
            repository.removeTournamentForAllUsers(tournamentID, playerEmailList)
        }
    }

    fun addTournamentForUser(tournamentID: String) {
        viewModelScope.launch {
            repository.addTournamentForUser(tournamentID)
        }
    }
}