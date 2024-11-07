package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.cse5236mobileapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [JoinTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JoinTournamentFragment : Fragment(R.layout.fragment_join_tournament) {

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Implementation done here
        val joinCodeField = view.findViewById<EditText>(R.id.textView)
        val backButton = view.findViewById<Button>(R.id.btnExitJoin)
        val joinButton = view.findViewById<Button>(R.id.btnJoinTournament)


        backButton.setOnClickListener() {
            parentFragmentManager.popBackStack()
        }
    }




    companion object {
        final val TAG = "Join Tournament Fragment"
    }
}