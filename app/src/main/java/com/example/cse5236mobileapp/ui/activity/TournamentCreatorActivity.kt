package com.example.cse5236mobileapp.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

class TournamentCreatorActivity : AppCompatActivity() {

    private val tournamentViewModel = TournamentViewModel()
    private val tournamentUserViewModel = TournamentUserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tournament_creator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tournamentNameField = findViewById<EditText>(R.id.etTournamentName)
        val numPlayerSpinner = findViewById<Spinner>(R.id.spinnerPlayers)
        val eventTypeSpinner = findViewById<Spinner>(R.id.spinnerEventType)
        val dateNameField = findViewById<EditText>(R.id.etDate)
        val timeNameField = findViewById<EditText>(R.id.etTime)
        val addressNameField = findViewById<EditText>(R.id.etAddress)
        val privateOrPublic = findViewById<RadioGroup>(R.id.rgPublicPrivate)
        val rulesField = findViewById<EditText>(R.id.etRules)
        val participants = findViewById<EditText>(R.id.etPlayers)


        val backButton = findViewById<Button>(R.id.tourMakerBackButton)
        val submitButton = findViewById<Button>(R.id.tourCreatorBtnSubmit)
        var currentRadioButton = false
        privateOrPublic.setOnCheckedChangeListener { _, idChecked ->
            when (idChecked) {
                R.id.rbPublic -> {
                    currentRadioButton = false
                }

                R.id.rbPrivate -> {
                    currentRadioButton = true
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
        submitButton.setOnClickListener {
            val stringTournamentName: String = tournamentNameField.text.toString()
            val stringDate: String = dateNameField.text.toString()
            val stringTime: String = timeNameField.text.toString()
            val stringAddress: String = addressNameField.text.toString()
            val stringRules: String = rulesField.text.toString()
            val stringAmountPlayers: String = numPlayerSpinner.selectedItem.toString()
            val stringParticipants: String = participants.text.toString().trim()
            var participantsArr = mutableListOf<String>()
            if (stringParticipants != "") {
                participantsArr =
                    stringParticipants.split(",").map { it.trim() }.toMutableList()
            }
            val currentUserEmail = tournamentUserViewModel.currentUserEmail()


            if (currentUserEmail != null) {
                participantsArr.add(currentUserEmail)
            }


            if (!(stringTournamentName.isBlank() || stringDate.isBlank() || stringTime.isBlank() || stringAddress.isBlank() || stringRules.isBlank())) {
                if (participantsArr.size <= stringAmountPlayers.toInt()) {
                    // Could potentially add loading dialog here

                    if (tournamentUserViewModel.verifyEmailList(participantsArr)) {
                        val tournamentVal = Tournament(
                            tournamentName = tournamentNameField.text.toString(),
                            date = dateNameField.text.toString(),
                            time = timeNameField.text.toString(),
                            address = addressNameField.text.toString(),
                            rules = rulesField.text.toString(),
                            numberPlayers = numPlayerSpinner.selectedItem.toString(),
                            eventType = eventTypeSpinner.selectedItem.toString(),
                            isPrivate = currentRadioButton,
                            isMorning = false,
                            players = participantsArr
                        )

                        // Creating initial games and code
                        tournamentVal.createInitialGames()
                        tournamentVal.generateJoinCode()

                        // Creating tournament object
                        val tournamentCode = tournamentViewModel.addTournament(tournamentVal)

                        // Linking tournament to users
                        tournamentUserViewModel.addTournamentForUser(
                            tournamentCode,
                            participantsArr
                        )

                        Toast.makeText(this, "Successfully created tournament", Toast.LENGTH_SHORT)
                            .show()
                        finish()

                    } else {
                        Toast.makeText(this, "Unable to verify Email List", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Too many people added to the tournament",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(this, "Missing fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}