package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [JoinTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JoinTournamentFragment : Fragment(R.layout.fragment_join_tournament) {

    val tournamentUserViewModel = TournamentUserViewModel()
    val tournamentViewModel = TournamentViewModel()

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Implementation done here
        val joinCodeField = view.findViewById<EditText>(R.id.txtJoinCode)
        val backButton = view.findViewById<Button>(R.id.btnExitJoin)
        val joinButton = view.findViewById<Button>(R.id.btnJoinTournament)


        backButton.setOnClickListener() {
            parentFragmentManager.popBackStack()
        }

        joinButton.setOnClickListener() {
            val userJoinCode = joinCodeField.text.toString().uppercase()
            tournamentViewModel.findTourneyIdFromJoinCode(userJoinCode) { tournamentIdentifier ->
                if (tournamentIdentifier != null) {
                    // TODO: Implementation to check players in tournament and then add user to it
                    Toast.makeText(requireContext(), "Able to find tournament with that join code", Toast.LENGTH_SHORT)
                        .show()

                    // Now will check to see if tournament if full
                    if (!tournamentIdentifier.tournament.isTournamentFull()) {
                        Toast.makeText(requireContext(), "Tournament has available space", Toast.LENGTH_SHORT).show()
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
        final val TAG = "Join Tournament Fragment"
    }
}