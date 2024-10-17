package com.example.cse5236mobileapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {
    companion object {
        private const val TAG = "Account Settings Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating Firebase reference
        var user = Firebase.auth.currentUser

        // Getting view elements here
        val backButton = view.findViewById<Button>(R.id.settingsBackButton)
        val submitButton = view.findViewById<Button>(R.id.settingsSubmitButton)
        val newDisplayName = view.findViewById<EditText>(R.id.settingsEditUsername)
        val newPasswordTextField = view.findViewById<EditText>(R.id.settingsEditPassword)
        val reenteredPasswordTextField = view.findViewById<EditText>(R.id.settingsReenterPassword)

        backButton.setOnClickListener(){
            Toast.makeText(requireContext(), user?.displayName, Toast.LENGTH_LONG).show()
            parentFragmentManager.beginTransaction().remove(this).commit()
            Log.i(TAG, "Going to Home Screen from Account Settings")
        }

        submitButton.setOnClickListener(){
            var newDisplayName = newDisplayName.text.toString()
            var newPassword = newPasswordTextField.text.toString()
            var reenteredPassword = reenteredPasswordTextField.text.toString()


        }


    }

}