package com.example.cse5236mobileapp.model

import com.google.firebase.firestore.PropertyName

data class TournamentUser (
    var userEmail: String,
    var userTournaments: MutableMap<String, String>,
)
{
    // no methods needed here yet?
}