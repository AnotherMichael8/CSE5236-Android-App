package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Account
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
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

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            Log.i(TAG, "Going to Log In from Create Account")
        }

        createButton.setOnClickListener {
            // Getting values for email and password
            var username = createUserField.text.toString()
            var password = createPassword.text.toString()
            var reentered = reenterPassword.text.toString()


            // See if fields are valid
            if (Account.checkCreateUserInfo(requireContext(), username, password, reentered)) {

                val tournamentUser = TournamentUser(
                    userEmail = username,
                    userTournaments = mutableListOf<String>()
                )
                tournamentUserViewModel.addUser(tournamentUser, password)
//                Account.createAccount(auth, requireActivity(), username, password) { success ->
//                    if (success) {
//                        val user = auth.currentUser
//                        database.collection("Users").document(username).set(mapOf<String, String>())
//                        // TODO: updateUI(user)
//                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
//
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "Authentication failed.",
//                            Toast.LENGTH_SHORT,
//                        ).show()
//                    }
//                }
            }
        }
    }
}