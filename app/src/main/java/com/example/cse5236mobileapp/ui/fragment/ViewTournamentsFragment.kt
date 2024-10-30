package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.Tournament
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewTournamentsFragment : Fragment(R.layout.fragment_view_tournaments) {
    companion object {
        private const val TAG = "View Tournaments Fragment"
    }

    var todayTournaments = arrayListOf<TournamentIdentifier>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val previousTournaments = arrayOf("Previous Tournament 1", "Previous Tournament 2", "Previous Tournament 3", "Previous Tournament 4")
        val futureTournaments = arrayOf("Future Tournament 1", "Future Tournament 2", "Future Tournament 3", "Future Tournament 4")

        val tournamentContainer: LinearLayout = view.findViewById(R.id.tournamentContainer)
        val database = Firebase.firestore

        // TODO: val docRef = database.collection("Tournaments").document("IDs for Tournaments (Template")

        Tournament.getTournamentList { tournaments ->
            todayTournaments = tournaments
            updateView(tournamentContainer)
        }

        val backButton = view.findViewById<Button>(R.id.viewTournamentBackButton)
        val user = FirebaseAuth.getInstance().currentUser

        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().remove(this).commit()
            Log.i(TAG, "Going to Home Screen from Account Settings")
        }
    }

    // Function to reduce duplication by updating the view with tournament data
    fun updateView(tournamentContainer: LinearLayout) {
        // Clear the container first, to avoid duplicate views on repeated updates
        tournamentContainer.removeAllViews()

        // Create and add a TextView for each tournament
        for (tournament in todayTournaments) {
            val tournamentView = Button(requireContext()).apply {
                text = tournament.tournament.tournamentName
                setPadding(16, 16, 16, 16) // Add some padding
                textSize = 18f // Set text size
                // Set layout parameters for width and height
                layoutParams = LinearLayout.LayoutParams(
                    0, // Width set to 0dp (for weight-based layouts)
                    LinearLayout.LayoutParams.WRAP_CONTENT // Height
                ).apply {
                    weight = 1f // This ensures equal width distribution
                }

                // Optional: Set OnClickListener if needed
                setOnClickListener {
                    // Handle click event
                    val modifyTournamentFragment = ModifyTournamentsFragment(tournament)
                    parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, modifyTournamentFragment).commit()
                }
            }
            val tournamentGames = Button(requireContext()).apply{
                text = "Games"
                setPadding(16, 16, 16, 16) // Add some padding
                textSize = 18f // Set text size
                // Optional: Set OnClickListener if needed
                setOnClickListener {
                    // Handle click event
                    parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewGamesFragment(tournament)).commit()
                }

            }
            // Add a HorizontalLinearLayout with the tournament and its games inside to the LinearLayout
            val tournamentAndGameHolder = LinearLayout(requireContext())
            tournamentAndGameHolder.addView(tournamentView)
            tournamentAndGameHolder.addView(tournamentGames)
            tournamentContainer.addView(tournamentAndGameHolder)
        }
    }
}