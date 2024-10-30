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
            var username = usernameField.text.toString()
            var password = passwordField.text.toString()

            // Checking if login fields are not empty
            if (Account.checkLoginInputs(requireContext(), username, password)) {
                // Contacting server to login
                Account.login(auth, requireActivity(), username, password) { success ->
                    if (success) {
                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Login Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val userAccount = Account(user?.uid, username)
                        val intent =
                            Intent(requireContext(), HomeScreenActivity::class.java).apply {
                                putExtra("user", userAccount)
                            }
                        startActivity(intent)
                        Log.i(TAG, "Switching to Home Screen")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Login Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
            else {
                Toast.makeText(requireContext(), "Missing fields", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCreateNew.setOnClickListener {
            val CreateAccountFrag = CreateAccountFragment()

            // Actually replace fragment
            parentFragmentManager.beginTransaction().replace(R.id.frgLoginContainer, CreateAccountFrag).addToBackStack(null).commit()
            Log.i(TAG, "Switching to Create Account")
        }
    }
}