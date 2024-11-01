package com.example.cse5236mobileapp.model

import com.google.firebase.firestore.PropertyName

data class TournamentUser (
    @PropertyName("UserEmail") var userEmail: String,
    @PropertyName("UserTournaments") var userTournaments: MutableList<String>,
)
{
    // no methods needed here yet?
}