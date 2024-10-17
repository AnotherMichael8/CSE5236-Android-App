package com.example.cse5236mobileapp

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class ViewTournamentsFragment : Fragment(R.layout.fragment_view_tournaments) {
    companion object {
        private const val TAG = "View Tournaments Fragment"
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val previousTournaments = arrayOf("Previous Tournament 1", "Previous Tournament 2", "Previous Tournament 3", "Previous Tournament 4")
        val todayTournaments = arrayOf("Tournament 1", "Tournament 2", "Tournament 3", "Tournament 4")
        val futureTournaments = arrayOf("Future Tournament 1", "Future Tournament 2", "Future Tournament 3", "Future Tournament 4")

        val tournamentContainer: LinearLayout = view.findViewById(R.id.tournamentContainer)

        for (tournament in todayTournaments) {
            val tournamentView = TextView(this).apply {
                text = tournament
                setPadding(16, 16, 16, 16) // Add some padding
                textSize = 18f // Set text size

                // Optional: Set OnClickListener if needed
                setOnClickListener {
                    // Handle click event
                }
            }
            // Add the TextView to the LinearLayout
            tournamentContainer.addView(tournamentView)
        }

    }
}