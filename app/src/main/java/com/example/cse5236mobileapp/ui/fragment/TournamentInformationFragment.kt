package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.PlayerNameAdapter
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentUsernameViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TournamentInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TournamentInformationFragment(private val tournamentIdentifier: TournamentIdentifier) : Fragment() {

    private val tournamentGamesViewModel =  TournamentGamesViewModel(tournamentIdentifier.tournamentId)
    private var tournamentUsernameViewModel = TournamentUsernameViewModel(mutableListOf<String>())
    private lateinit var playerNameAdapter: PlayerNameAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the view of the tournament
        val tournamentLayout = view.findViewById<LinearLayout>(R.id.tournamentViewLinear)

        val playerNameRecyclerView = view.findViewById<RecyclerView>(R.id.rvPlayersInTourney)

        // Initializing adapter
        playerNameAdapter = PlayerNameAdapter(mutableListOf())
        playerNameRecyclerView.adapter = playerNameAdapter
        playerNameRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        tournamentUsernameViewModel.emailToUsername.observe(viewLifecycleOwner, Observer { tournamentMap ->
            if (tournamentMap != null) {
                updatePlayerNames(tournamentMap.values.toList())
            }
        })


        tournamentGamesViewModel.tournamentLive.observe(viewLifecycleOwner, Observer { tournament ->
            if (tournament != null) {
                tournamentUsernameViewModel.updateEmailList(tournament.players)
                updateView(tournament, tournamentLayout)
            }
        })

        // TODO: Add on click listener for buttons

        view.findViewById<Button>(R.id.btnTournamentViewBack).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()
        }

        view.findViewById<Button>(R.id.btnEditTournament).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ModifyTournamentsFragment(tournamentIdentifier))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateView(tournament: Tournament, linearLayout: LinearLayout) {
        val tournamentName = linearLayout.findViewById<TextView>(R.id.txtTournamentName)
        val address = linearLayout.findViewById<TextView>(R.id.txtViewAddress)
        val date = linearLayout.findViewById<TextView>(R.id.txtViewDate)
        val time = linearLayout.findViewById<TextView>(R.id.txtViewTime)
        val numPlayers = linearLayout.findViewById<TextView>(R.id.txtViewNumberPlayers)
        val rules = linearLayout.findViewById<TextView>(R.id.txtViewRules)
        val joinCode = linearLayout.findViewById<TextView>(R.id.txtJoinCodeDisplay)

        tournamentName.text = tournament.tournamentName
        address.text = tournament.address
        date.text = tournament.date
        if (tournament.isMorning) {
            time.text = "${tournament.time} am"
        }
        else {
            time.text = "${tournament.time} pm"
        }

        numPlayers.text = tournament.numberPlayers
        rules.text = tournament.rules
        joinCode.text = tournament.joinCode

    }

    private fun updatePlayerNames(playerNames: List<String>) {
        //playerNameAdapter
        playerNameAdapter.updateNames(playerNames)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_information, container, false)
    }


}