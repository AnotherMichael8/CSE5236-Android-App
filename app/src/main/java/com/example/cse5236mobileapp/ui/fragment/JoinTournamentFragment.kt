package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [JoinTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JoinTournamentFragment : Fragment(R.layout.fragment_join_tournament) {

    private val tournamentUserViewModel = TournamentUserViewModel()
    private val tournamentViewModel = TournamentViewModel()

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Implementation done here
        val joinCodeField = view.findViewById<EditText>(R.id.txtJoinCode)
        val backButton = view.findViewById<Button>(R.id.btnExitJoin)
        val joinButton = view.findViewById<Button>(R.id.btnJoinTournament)


        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        joinButton.setOnClickListener {
            val userJoinCode = joinCodeField.text.toString().uppercase()
            tournamentViewModel.findTourneyIdFromJoinCode(userJoinCode) { tournamentIdentifier ->
                if (tournamentIdentifier != null) {
                    // Now will check to see if tournament if full
                    if (!tournamentIdentifier.tournament.isTournamentFull()) {
                        val currentUser = tournamentUserViewModel.currentUserEmail()
                        if (!tournamentIdentifier.tournament.isUserAPlayer(currentUser)) {
                            Toast.makeText(requireContext(), "User isn't already in tournament", Toast.LENGTH_SHORT).show()
                            // TODO: Update the tournament to have the user be in it and then add tournament ID to user
                            // User added to tournament
                            tournamentViewModel.addUserToTournament(tournamentIdentifier.tournamentId, tournamentIdentifier.tournament.players)
                            // Tournament added to user
                            tournamentUserViewModel.addTournamentForUser(tournamentIdentifier.tournamentId)
                        }
                        else {
                            Toast.makeText(requireContext(), "User is already in tournament", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(requireContext(), "Tournament full", Toast.LENGTH_SHORT).show()
                    }


                }
                else {
                    Toast.makeText(requireContext(), "Not able to find tournament with that code", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }




    companion object {
        private const val TAG = "Join Tournament Fragment"
    }
}