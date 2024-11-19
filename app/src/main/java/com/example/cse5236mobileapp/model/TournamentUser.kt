package com.example.cse5236mobileapp.model

data class TournamentUser (
    var displayName: String,
    var userEmail: String,
    var userTournaments: MutableMap<String, String>,
)
{
    // no methods needed here yet?
}