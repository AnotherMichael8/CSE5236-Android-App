package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.viewmodel.TournamentGamesViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TournamentInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TournamentInformationFragment(private val tournamentIdentifier: TournamentIdentifier) : Fragment() {

    private val tournamentGamesViewModel =  TournamentGamesViewModel(tournamentIdentifier.tournamentId)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the view of the tournament
        val tournamentLayout = view.findViewById<LinearLayout>(R.id.tournamentViewLinear)


        tournamentGamesViewModel.tournamentLive.observe(viewLifecycleOwner, Observer { tournament ->
            if (tournament != null) {
                updateView(tournament, tournamentLayout)
            }
        })

        // TODO: Add on click listener for buttons
    }

    private fun updateView(tournament: Tournament, linearLayout: LinearLayout) {
        val tournamentName = linearLayout.findViewById<TextView>(R.id.txtTournamentName)
        val address = linearLayout.findViewById<TextView>(R.id.txtViewAddress)
        val date = linearLayout.findViewById<TextView>(R.id.txtViewDate)
        val time = linearLayout.findViewById<TextView>(R.id.txtViewTime)
        val numPlayers = linearLayout.findViewById<TextView>(R.id.txtViewNumberPlayers)
        val rules = linearLayout.findViewById<TextView>(R.id.txtViewRules)

        tournamentName.text = tournament.tournamentName
        address.text = tournament.address
        date.text = tournament.date
        time.text = tournament.time
        numPlayers.text = tournament.numberPlayers
        rules.text = tournament.rules

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_information, container, false)
    }


}