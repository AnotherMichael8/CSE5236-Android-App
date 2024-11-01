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

    val usersLive: MutableLiveData<List<TournamentUser>> by lazy {
        MutableLiveData<List<TournamentUser>>()
    }

    //Might not be needed yet... NOTE: still WIP

    init {
        val users = mutableSetOf<TournamentUser>()
        firestore.collection("Users").addSnapshotListener{ query, exception ->
            if (exception != null) {
                usersLive.value = emptyList()
                Log.w(TAG, "Listen Failed", exception)
                return@addSnapshotListener
            }

            if (query != null && !query.isEmpty){
                users.clear()
                for (document in query.documents) {
                    users.add(TournamentUser(document.id, mutableListOf<String>()))
                }
                usersLive.value = users.toList()
            }

        }
    }

    // Calling addUserToDatabase from repository
    fun addUser(tUser: TournamentUser, password: String) {
        repository.addUserToDatabase(tUser, password)
    }

    // Calling modifyEmail from repository
    fun modifyUserEmail(firebaseUser: FirebaseUser, tUser: TournamentUser, newEmail: String) {
        repository.modifyEmail(firebaseUser, tUser, newEmail)
    }

    // Calling modifyPassword from repository
    fun modifyUserPassword(firebaseUser: FirebaseUser, newPassword: String) {
        repository.modifyPassword(firebaseUser, newPassword)
    }

    // Calling deleteUser from repository
    fun deleteUser(firebaseUser: FirebaseUser, tUser: TournamentUser){
        repository.deleteUser(firebaseUser, tUser)
    }

}