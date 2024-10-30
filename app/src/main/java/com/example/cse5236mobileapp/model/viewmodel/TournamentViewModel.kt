package com.example.cse5236mobileapp.model.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TournamentViewModel : ViewModel() {

    private val repository = TournamentRepository()
    private val database = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private val dbUser = user?.email ?: "No email"

    val tournaments: MutableLiveData<List<Tournament>> by lazy {
        MutableLiveData<List<Tournament>>()
    }

}