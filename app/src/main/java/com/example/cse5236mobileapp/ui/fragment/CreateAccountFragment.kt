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
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.ui.activity.HomeScreenActivity
import com.example.cse5236mobileapp.ui.fragment.LogInFragment.Companion
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {


    companion object {
        private const val TAG = "Create Account Fragment"
    }

    private val tournamentUserViewModel = TournamentUserViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating firebase reference
        var auth = Firebase.auth
        val database = FirebaseFirestore.getInstance()

        // Getting view elements here
        val backButton = view.findViewById<Button>(R.id.tourMakerBackButton)
        val createButton = view.findViewById<Button>(R.id.btnCreateAccount)
        val createUserField = view.findViewById<EditText>(R.id.ditCreateUsername)
        val createPassword = view.findViewById<EditText>(R.id.ditCreatePassword)
        val reenterPassword = view.findViewById<EditText>(R.id.ditReenterPassword)
        val createUserName = view.findViewById<EditText>(R.id.txtEnterUsername)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            Log.i(TAG, "Going to Log In from Create Account")
        }

        createButton.setOnClickListener {
            // Getting values for email and password
            val username = createUserField.text.toString()
            val password = createPassword.text.toString()
            val reentered = reenterPassword.text.toString()
            val displayName = createUserName.text.toString()


            // See if fields are valid
            if (Account.checkCreateUserInfo(requireContext(), username, password, reentered, displayName)) {
                tournamentUserViewModel.addUser(username, password, displayName) {success ->
                    if (success) {
                        // TODO: toast a successful account creation and then go to next activity
                        Toast.makeText(requireActivity(), "Account Creation successful for $username", Toast.LENGTH_LONG).show()

                        val intent =
                            Intent(requireContext(), HomeScreenActivity::class.java).apply {
                                putExtra("user", Account(auth.currentUser?.uid, username))
                            }
                        startActivity(intent)
                        Log.i(TAG, "Switching to Home Screen")
                    }
                    else {
                        // TODO: Toast a failure here
                        Toast.makeText(requireActivity(), "Account Creation failure for $username", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}