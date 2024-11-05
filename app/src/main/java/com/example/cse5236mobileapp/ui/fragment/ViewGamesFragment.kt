package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.ViewGameAdapter
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel

class ViewGamesFragment(private var tournamentIdentifier: TournamentIdentifier) : Fragment(R.layout.fragment_view_games) {
    companion object {
        private const val TAG = "View Games Fragment"
    }
    //TODO: all of the game functionality
    private val tournamentGamesViewModel = TournamentGamesViewModel(tournamentIdentifier.tournamentId)
    private lateinit var viewGameAdapter : ViewGameAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //values for future reference
        /*
        val gamesContainer = view.findViewById<LinearLayout>(R.id.gamesContainer)
         */
        val backButton = view.findViewById<Button>(R.id.viewGameBackButton)
        val gamesTextView = view.findViewById<TextView>(R.id.gameViewText)
        val nextRoundButton = view.findViewById<ImageButton>(R.id.btnNextRound)
        val previousRoundButton = view.findViewById<ImageButton>(R.id.btnPreviousRound)

        val rvGameView = view.findViewById<RecyclerView>(R.id.rvGameView)

        val numberRounds = tournamentIdentifier.tournament.getNumberOfRounds()

        viewGameAdapter = ViewGameAdapter(tournamentIdentifier, numberRounds)
        rvGameView.adapter = viewGameAdapter
        rvGameView.layoutManager = LinearLayoutManager(requireContext())


        // Livedata implementation here
        tournamentGamesViewModel.tournamentGamesLive.observe(viewLifecycleOwner, Observer { games ->
            updateGamesContainer(viewGameAdapter, games)
        })

        tournamentGamesViewModel.tournamentLive.observe(viewLifecycleOwner, Observer { tournament ->
            updateName(gamesTextView, tournament)
        })

        //back button functionality
        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }
        nextRoundButton.setOnClickListener() {
            viewGameAdapter.nextRound()
        }
        previousRoundButton.setOnClickListener() {
            viewGameAdapter.previousRound()
        }
    }

    private fun updateGamesContainer(viewGameAdapter: ViewGameAdapter, games: List<Game>) {
        viewGameAdapter.updateGames(games)
    }

    private fun updateName(textView: TextView, tournament: Tournament) {
        textView.text = tournament.tournamentName
    }
}