package com.example.cse5236mobileapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ViewGamesFragment(private var tournamentIdentifier: TournamentIdentifier) : Fragment(R.layout.fragment_view_games) {
    companion object {
        private const val TAG = "View Games Fragment"
    }
    //TODO: all of the game functionality
//    var tournamentGames = arrayListOf<Games>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<Button>(R.id.viewGameBackButton)
        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }

        val gamesTextView = view.findViewById<TextView>(R.id.gameViewText)
        gamesTextView.text = "Games for " + tournamentIdentifier.tournament.tournamentName
    }
}