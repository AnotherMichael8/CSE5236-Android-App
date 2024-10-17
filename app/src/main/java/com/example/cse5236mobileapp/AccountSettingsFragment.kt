package com.example.cse5236mobileapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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
        val user = FirebaseAuth.getInstance().currentUser

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

            // Check display name field, update if not empty
            if(newDisplayName.isNotEmpty()) {
                Account.updateDisplayName(newDisplayName, requireContext()) { success ->
                    if (success) {
                        (activity as HomeScreenActivity).updateWelcomeText(newDisplayName)
                    }
                }
            }

            // Check password fields and see if not empty and if match
            if(newPassword.isNotEmpty()) {
                if(newPassword.equals(reenteredPassword)) {
                    Account.updatePassword(newPassword, requireContext())
                    // TODO: Get it to not crash in this case, will just call finish for now
                    (activity as HomeScreenActivity).finish()
                }
            }
        }

        deleteButton.setOnClickListener() {
            Account.deleteAccount { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Deleted user, bye bye :(", Toast.LENGTH_LONG).show()
                    (activity as HomeScreenActivity).finish()
                }
            }
        }


    }

}