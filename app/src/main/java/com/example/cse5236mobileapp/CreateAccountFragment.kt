package com.example.cse5236mobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
 * Use the [CreateAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {


    companion object {
        private const val TAG = "Create Account Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating firebase reference
        var auth = Firebase.auth

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
                Account.createAccount(auth, requireActivity(), username, password) {success ->
                    if (success) {
                        val user = auth.currentUser
                        // TODO: updateUI(user)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }
}