package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.example.cse5236mobileapp.model.viewmodel.TournamentUsernameViewModel

class ViewGamesFragment(private var tournamentIdentifier: TournamentIdentifier) :
    Fragment(R.layout.fragment_view_games) {
    companion object {
        private const val TAG = "View Games Fragment"
    }

    //TODO: all of the game functionality
    private val tournamentGamesViewModel =
        TournamentGamesViewModel(tournamentIdentifier.tournamentId)
    private val tournamentUsernameViewModel = TournamentUsernameViewModel(mutableListOf())
    private lateinit var viewGameAdapter: ViewGameAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //values for future reference
        /*
        val gamesContainer = view.findViewById<LinearLayout>(R.id.gamesContainer)
         */
        val backButton = view.findViewById<Button>(R.id.viewGameBackButton)
        val gamesTextView = view.findViewById<TextView>(R.id.gameViewText)
        val btnGameViewRound = view.findViewById<TextView>(R.id.btnGameViewRound)
        val nextRoundButton = view.findViewById<ImageButton>(R.id.btnNextRound)
        val previousRoundButton = view.findViewById<ImageButton>(R.id.btnPreviousRound)

        val rvGameView = view.findViewById<RecyclerView>(R.id.rvGameView)

        val numberRounds = tournamentIdentifier.tournament.getNumberOfRounds()
        btnGameViewRound.text = Game.getRoundDisplayer(1, numberRounds)
        previousRoundButton.visibility = View.INVISIBLE

        viewGameAdapter = ViewGameAdapter(tournamentIdentifier, numberRounds)
        rvGameView.adapter = viewGameAdapter
        rvGameView.layoutManager = LinearLayoutManager(requireContext())

        tournamentUsernameViewModel.emailToUsername.observe(
            viewLifecycleOwner,
            Observer { playerNameMap ->
                if (playerNameMap != null) {
                    updatePlayerMap(playerNameMap)
                }
            })


        // Livedata implementation here
        tournamentGamesViewModel.tournamentGamesLive.observe(viewLifecycleOwner, Observer { games ->
            updateGamesContainer(viewGameAdapter, games)
        })

        tournamentGamesViewModel.tournamentLive.observe(viewLifecycleOwner, Observer { tournament ->
            // Updating the tournament usernames
            tournamentUsernameViewModel.updateEmailList(tournament.players)
            updatePlayers(tournament.players)
            updateName(gamesTextView, tournament)
        })

        tournamentGamesViewModel.currentUserAdminPrivileges.observe(viewLifecycleOwner, Observer { privileges ->
            viewGameAdapter.updateUserPrivileges(privileges)
        })

        //back button functionality
        backButton.setOnClickListener() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }
        nextRoundButton.setOnClickListener() {
            val curRound = viewGameAdapter.nextRound()
            btnGameViewRound.text = Game.getRoundDisplayer(curRound, numberRounds)
            if (curRound >= numberRounds) {
                nextRoundButton.visibility = View.INVISIBLE
            }
            if (curRound > 1) {
                previousRoundButton.visibility = View.VISIBLE
            }
        }
        previousRoundButton.setOnClickListener() {
            val curRound = viewGameAdapter.previousRound()
            btnGameViewRound.text = Game.getRoundDisplayer(curRound, numberRounds)
            if (curRound <= 1) {
                previousRoundButton.visibility = View.INVISIBLE
            }
            if (curRound < numberRounds) {
                nextRoundButton.visibility = View.VISIBLE
            }
        }
    }

    private fun updateGamesContainer(viewGameAdapter: ViewGameAdapter, games: List<Game>) {
        viewGameAdapter.updateGames(games)
    }

    private fun updateName(textView: TextView, tournament: Tournament) {
        textView.text = tournament.tournamentName
    }

    private fun updatePlayerMap(newPlayerMap: Map<String, String>) {
        viewGameAdapter.updatePlayerMap(newPlayerMap)
    }

    private fun updatePlayers(newPlayerList: List<String>) {
        viewGameAdapter.updatePlayerList(newPlayerList)
    }
}