package com.example.cse5236mobileapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log;
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
            if (checkLoginInputs(username, password)) {
                // Contacting server to login
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(
                                requireContext(),
                                "Login Success",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent(requireContext(), HomeScreenActivity::class.java).apply {
                                putExtra ("username", username)
                            }
                            startActivity(intent)
                            Log.i(TAG, "Switching to Home Screen")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),
                                "Login Failed",
                                Toast.LENGTH_SHORT,
                            ).show()
                            // TODO: updateUI(null)
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
            Log.i(TAG,"Switching to Create Account")
        }
    }

    fun checkLoginInputs (username: String, password: String): Boolean {
        // Checking the three fields to see if they're empty
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(requireContext(), "Missing Username", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Missing Password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}