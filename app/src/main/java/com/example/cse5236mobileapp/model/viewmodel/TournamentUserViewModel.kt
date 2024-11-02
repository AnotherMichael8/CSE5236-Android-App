package com.example.cse5236mobileapp.model.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.TournamentUserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TournamentUserViewModel : ViewModel() {

    companion object {
        private const val TAG = "TournamentUserViewModel"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val repository = TournamentUserRepository()

    val allUsersInfoLive: MutableLiveData<List<TournamentUser>> by lazy {
        MutableLiveData<List<TournamentUser>>()
    }

    //Might not be needed yet... NOTE: still WIP

    init {
        val users = mutableSetOf<TournamentUser>()
        firestore.collection("Users").addSnapshotListener{ query, exception ->
            if (exception != null) {
                allUsersInfoLive.value = emptyList()
                Log.w(TAG, "Listen Failed", exception)
                return@addSnapshotListener
            }

            if (query != null){
                users.clear()
                for (document in query.documents) {
                    val dataMap = document.data?.mapNotNull { (key, value) ->
                        if (value is String) key to value else null
                    }?.toMap()?.toMutableMap() ?: mutableMapOf()

                    val tournamentUserTemp = TournamentUser(document.id, dataMap)
                    users.add(tournamentUserTemp)
//                    TournamentUser(document.id, (document.data?.keys?.toMutableList() ?: mutableListOf<String>(), ))
//                    users.add(TournamentUser(document.id, document.data?.keys?.toMutableList() ?: mutableListOf<String>()))
                }
                allUsersInfoLive.value = users.toList()
            }
        }
    }

    // Calling addUserToDatabase from repository
    fun addUser(email: String, password: String) {
        repository.addUserToDatabase(email, password)
    }

    fun modifyUser(firebaseUser: FirebaseUser, newEmail: String, newPassword:String){
        repository.modifyUser(firebaseUser, newEmail, newPassword)
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
    fun deleteUser(firebaseUser: FirebaseUser, userEmail: String){
        repository.deleteUser(firebaseUser, userEmail)
    }

}