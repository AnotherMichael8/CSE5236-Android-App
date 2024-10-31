package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel

class ViewGamesFragment(private var tournamentIdentifier: TournamentIdentifier) : Fragment(R.layout.fragment_view_games) {
    companion object {
        private const val TAG = "View Games Fragment"
    }
    //TODO: all of the game functionality
    private val tournamentGamesViewModel = TournamentGamesViewModel(tournamentIdentifier.tournamentId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //values for future reference
        val gamesContainer = view.findViewById<LinearLayout>(R.id.gamesContainer)
        val backButton = view.findViewById<Button>(R.id.viewGameBackButton)
        val gamesTextView = view.findViewById<TextView>(R.id.gameViewText)

        // Livedata implementation here
        tournamentGamesViewModel.tournamentLive.observe(viewLifecycleOwner, Observer { tournament ->
            updateName(gamesTextView, tournament)
            updateGamesContainer(gamesContainer, tournament)
        })

        //back button functionality
        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }
    }

    fun updateGamesContainer(gamesContainer: LinearLayout, tournament: Tournament) {
        gamesContainer.removeAllViews()
        for (game in tournament.games) {
            // Setup needed modules to makeup each game
            val gameLayout = FrameLayout(requireContext()).apply {
                // Assign an ID to each FrameLayout for fragment transactions
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16) // Add some margins if needed
                }
            }

            // Add the FrameLayout to the container
            gamesContainer.addView(gameLayout)

            // Add a fragment for each game using the FrameLayout as its container
            parentFragmentManager.beginTransaction()
                .replace(gameLayout.id, GameFragment(game))
                .commit()
        }
    }

    fun updateName(textView: TextView, tournament: Tournament) {
        textView.text = "Games for " + tournament.tournamentName
    }
}