package com.example.cse5236mobileapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log;

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button in the fragment's layout
        val buttonContinue = view.findViewById<Button>(R.id.btnLogIn)

        val buttonCreateNew = view.findViewById<Button>(R.id.btnCreateNew)

        // Set the click listener on the button
        buttonContinue.setOnClickListener {
            val intent = Intent(requireContext(), HomeScreenActivity::class.java)
            startActivity(intent)
            Log.i("Success", "Switching to Home Screen")
        }

        buttonCreateNew.setOnClickListener {
            val CreateAccountFrag = CreateAccountFragment()

            // Actually replace fragment
            parentFragmentManager.beginTransaction().replace(R.id.frgLoginContainer, CreateAccountFrag).addToBackStack(null).commit()
            Log.i("Success","Switching to Create Account")
        }
    }

}