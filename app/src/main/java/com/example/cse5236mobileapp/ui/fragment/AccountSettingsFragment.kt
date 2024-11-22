package com.example.cse5236mobileapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import androidx.core.os.LocaleListCompat

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
        tournamentUserViewModel.allUsersInfoLive.observe(viewLifecycleOwner) { tournamentUsers ->
            userTournamentList.clear()
            for(tournamentUser in tournamentUsers){
                if(tournamentUser.userEmail == user.email){
                    userTournamentList.addAll(tournamentUser.userTournaments.keys)
                    break
                }
            }
        }

        tournamentViewModel.userTournamentLive.observe(viewLifecycleOwner) { tournaments ->
            userTournamentIdentifierList.clear()
            userTournamentIdentifierList.addAll(tournaments)
        }

        // Getting view elements here
        val backButton = view.findViewById<Button>(R.id.settingsBackButton)
        val submitButton = view.findViewById<Button>(R.id.settingsSubmitButton)
        val newDisplayNameText = view.findViewById<EditText>(R.id.settingsEditUsername)
        val newPasswordTextField = view.findViewById<EditText>(R.id.settingsEditPassword)
        val reenteredPasswordTextField = view.findViewById<EditText>(R.id.settingsReenterPassword)
        val deleteButton = view.findViewById<Button>(R.id.btnDeleteAccount)

        backButton.setOnClickListener{
            parentFragmentManager.beginTransaction().remove(this).commit()
            Log.i(TAG, "Going to Home Screen from Account Settings")
        }

        submitButton.setOnClickListener{
            val newDisplayName = newDisplayNameText.text.toString()
            val newPassword = newPasswordTextField.text.toString()
            val reenteredPassword = reenteredPasswordTextField.text.toString()

            if(newPassword == reenteredPassword && (newDisplayName.isNotBlank() || newPassword.isNotBlank())){
                tournamentUserViewModel.modifyUser(user, newDisplayName, newPassword)
                parentFragmentManager.popBackStack()
            }
        }

        deleteButton.setOnClickListener {
            //Using User
            tournamentUserViewModel.deleteUser(user, user.email!!) { result ->
                if (result) {
                    (activity as HomeScreenActivity).finish()
                    Toast.makeText(requireContext(), "User ${user.email} is deleted.", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(requireContext(), "Unable to delete ${user.email}.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}