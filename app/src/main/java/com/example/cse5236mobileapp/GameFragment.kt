package com.example.cse5236mobileapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class GameFragment(private val game: Game) : Fragment(R.layout.fragment_game){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val team1TextView = view.findViewById<TextView>(R.id.team1TextView)
        val team2TextView = view.findViewById<TextView>(R.id.team2TextView)
        val team1ScoreTextView = view.findViewById<TextView>(R.id.team1ScoreTextView)
        val team2ScoreTextView = view.findViewById<TextView>(R.id.team2ScoreTextView)

        team1TextView.text = game.teamOne
        team2TextView.text = game.teamTwo
        team1ScoreTextView.text = "Score: " + game.teamOneScore.toString()
        team2ScoreTextView.text = "Score: " + game.teamTwoScore.toString()

    }
}