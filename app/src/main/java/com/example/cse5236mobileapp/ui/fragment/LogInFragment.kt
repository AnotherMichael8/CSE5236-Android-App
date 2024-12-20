package com.example.cse5236mobileapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Account
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment(R.layout.fragment_log_in) {
    companion object {
        private const val TAG = "Log In Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = Firebase.auth
        // Find the button in the fragment's layout
        val buttonContinue = view.findViewById<Button>(R.id.btnLogIn)
        val buttonCreateNew = view.findViewById<Button>(R.id.btnCreateNew)

        val usernameField = view.findViewById<EditText>(R.id.ditUsername)
        val passwordField = view.findViewById<EditText>(R.id.ditUserPassword)


        // Set the click listener on the button
        buttonContinue.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            // Checking if login fields are not empty
            if (Account.checkLoginInputs(requireContext(), username, password)) {
                // Contacting server to login
                Account.login(auth, username, password) { success ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Login Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                        goToHomeScreen()
                        Log.i(TAG, "Switching to Home Screen")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Login Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Missing fields", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCreateNew.setOnClickListener {
            val createAccountFrag = CreateAccountFragment()

            // Actually replace fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.frgLoginContainer, createAccountFrag).addToBackStack(null).commit()
            Log.i(TAG, "Switching to Create Account")
        }

        // Go straight to home screen if user is already logged in
        if (FirebaseAuth.getInstance().currentUser != null) {
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        val intent =
            Intent(requireContext(), HomeScreenActivity::class.java)
        startActivity(intent)
    }
}