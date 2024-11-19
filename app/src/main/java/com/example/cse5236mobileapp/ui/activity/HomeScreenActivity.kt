package com.example.cse5236mobileapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.cse5236mobileapp.ui.fragment.AccountSettingsFragment
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.ui.fragment.ViewTournamentsFragment
import com.example.cse5236mobileapp.model.Account
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeScreenActivity : AppCompatActivity() {
    lateinit var user: Account
    lateinit var currentUser: FirebaseUser


    val tournamentUserViewModel = TournamentUserViewModel()
    lateinit var button_logout : Button
    lateinit var settings_button : ImageButton
    lateinit var view_tournament_button : Button
    lateinit var tournamentCreator_button : Button
    lateinit var find_tournament_button : Button


    companion object {
        private const val TAG = "Home Screen Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)


        tournamentUserViewModel.usernameLive.observe(this) { userDisplayName ->
            updateWelcomeText(userDisplayName)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        button_logout = findViewById<Button>(R.id.LogoutButton)
        settings_button = findViewById<ImageButton>(R.id.settingsButton)
        view_tournament_button = findViewById<Button>(R.id.ViewButton)
        tournamentCreator_button = findViewById<Button>(R.id.CreateButton)
        find_tournament_button = findViewById<Button>(R.id.LocationsButton)

        button_logout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
            Log.i(TAG, "Logging Out")
        }

        settings_button.setOnClickListener {
            val settingsFrag = AccountSettingsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frgHomeScreenContainer, settingsFrag)
                .addToBackStack(null)
                .commit()
            Log.i(TAG, "Going to Settings Fragment")
        }

        view_tournament_button.setOnClickListener {
            Log.i(TAG, "Going to View Tournaments Fragment")
            val viewTournamentsFrag = ViewTournamentsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frgHomeScreenContainer, viewTournamentsFrag)
                .addToBackStack(null)
                .commit()
        }
        tournamentCreator_button.setOnClickListener {
            val intent = Intent(this, TournamentCreatorActivity::class.java)
            startActivity(intent)
        }

        find_tournament_button.setOnClickListener{
            try {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception){
                Log.e(TAG, "Error starting MapsActivity: ${e.message}", e)

                Toast.makeText(this, "An error occurred. Pleas try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy()
    {
        super.onDestroy()
        button_logout.setOnClickListener(null)
        settings_button.setOnClickListener(null)
        view_tournament_button.setOnClickListener(null)
        tournamentCreator_button.setOnClickListener(null)
        find_tournament_button.setOnClickListener(null)
    }

    @SuppressLint("SetTextI18n")
    fun updateWelcomeText(displayName: String) {
        findViewById<TextView>(R.id.txtHomeScreenWelcome).text = "Welcome: $displayName"
    }
}