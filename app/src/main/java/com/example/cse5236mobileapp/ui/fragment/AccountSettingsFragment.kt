package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Account
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.example.cse5236mobileapp.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {
    companion object {
        private const val TAG = "Account Settings Fragment"
    }

    private val tournamentViewModel = TournamentViewModel()
    private val tournamentUserViewModel = TournamentUserViewModel()
    private val userTournamentList = mutableListOf<String>()
    private val userTournamentIdentifierList = mutableListOf<TournamentIdentifier>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating Firebase reference
        val user = FirebaseAuth.getInstance().currentUser!!
        val database = Firebase.firestore
        tournamentUserViewModel.allUsersInfoLive.observe(viewLifecycleOwner, Observer { tournamentUsers ->
            userTournamentList.clear()
            for(tournamentUser in tournamentUsers){
                if(tournamentUser.userEmail == user!!.email){
                    userTournamentList.addAll(tournamentUser.userTournaments.keys)
                    break
                }
            }
        })

        tournamentViewModel.userTournamentLive.observe(viewLifecycleOwner, Observer { tournaments ->
            userTournamentIdentifierList.clear()
            userTournamentIdentifierList.addAll(tournaments)
        })

        // Getting view elements here
        val backButton = view.findViewById<Button>(R.id.settingsBackButton)
        val submitButton = view.findViewById<Button>(R.id.settingsSubmitButton)
        val newDisplayNameText = view.findViewById<EditText>(R.id.settingsEditUsername)
        val newPasswordTextField = view.findViewById<EditText>(R.id.settingsEditPassword)
        val reenteredPasswordTextField = view.findViewById<EditText>(R.id.settingsReenterPassword)
        val deleteButton = view.findViewById<Button>(R.id.btnDeleteAccount)

        backButton.setOnClickListener(){
            val displayName = user?.email ?: "No display name available"
            Toast.makeText(requireContext(), displayName, Toast.LENGTH_LONG).show()
            parentFragmentManager.beginTransaction().remove(this).commit()
            Log.i(TAG, "Going to Home Screen from Account Settings")
        }

        submitButton.setOnClickListener(){
            var newDisplayName = newDisplayNameText.text.toString()
            var newPassword = newPasswordTextField.text.toString()
            var reenteredPassword = reenteredPasswordTextField.text.toString()

            if(newPassword == reenteredPassword && (!newDisplayName.isNullOrBlank() || !newPassword.isNullOrBlank())){
                tournamentUserViewModel.modifyUser(user, newDisplayName, newPassword)
                (activity as HomeScreenActivity).finish()
            }
        }

        deleteButton.setOnClickListener() {
            //Using User
            if (user != null) {
                tournamentUserViewModel.deleteUser(user, user.email!!)
                (activity as HomeScreenActivity).finish()
            } else {
                Log.e(TAG, "User deletion failed: user does not exist")
            }
        }


    }

}