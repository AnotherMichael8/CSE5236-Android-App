package com.example.cse5236mobileapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewGamesFragment(private var tournamentIdentifier: TournamentIdentifier) : Fragment(R.layout.fragment_view_games) {
    companion object {
        private const val TAG = "View Games Fragment"
    }
    //TODO: all of the game functionality
    var tournamentGames = listOf<Game>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //values for future reference
        val gamesContainer = view.findViewById<LinearLayout>(R.id.gamesContainer)
        val backButton = view.findViewById<Button>(R.id.viewGameBackButton)
        val gamesTextView = view.findViewById<TextView>(R.id.gameViewText)

        //set title text to be respective to selected tournament
        gamesTextView.text = "Games for " + tournamentIdentifier.tournament.tournamentName

        //back button functionality
        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }

        //retrieve list of games in tournament
        tournamentGames = tournamentIdentifier.tournament.games
        gamesContainer.removeAllViews()
        for (game in tournamentGames) {
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
}