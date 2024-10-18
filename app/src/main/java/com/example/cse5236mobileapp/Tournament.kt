package com.example.cse5236mobileapp

import com.google.firebase.firestore.PropertyName

data class Tournament (
    @PropertyName("Address") val address: String = "",
    @PropertyName("Date") val date: String = "",
    @PropertyName("EventType") val eventType: String = "",
    @PropertyName("NumberPlayers") val numberPlayers: String = "",
    @PropertyName("Rules") val rules: String = "",
    @PropertyName("Time") val time: String = "",
    @PropertyName("TournamentName") val tournamentName: String = "",
    @PropertyName("isMorning") val isMorning: Boolean = false,
    @PropertyName("isPrivate") val isPrivate: Boolean = false
)