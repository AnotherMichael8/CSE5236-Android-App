package com.example.cse5236mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.internal.Objects
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.util.UUID

class TournamentCreator : AppCompatActivity() {
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
        var currentRadioButton : Boolean = false
        privateOrPublic.setOnCheckedChangeListener { radioGroup, idChecked ->
            when(idChecked)
            {
                R.id.rbPublic ->
                {
                    currentRadioButton = false
                }
                R.id.rbPrivate ->
                {
                    currentRadioButton = true
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
        submitButton.setOnClickListener {
            val stringTournamentName : String = tournamentNameField.text.toString()
            val stringDate : String = dateNameField.text.toString()
            val stringTime : String = timeNameField.text.toString()
            val stringAddress : String = addressNameField.text.toString()
            val stringRules : String = rulesField.text.toString()
            val stringAmountPlayers: String = numPlayerSpinner.selectedItem.toString()
            val stringParticipants : String = participants.text.toString().trim()
            val participantsArr = stringParticipants.split(",")

            if(participantsArr.size == stringAmountPlayers.toInt() &&
                !(stringTournamentName.isBlank() || stringDate.isBlank() || stringTime.isBlank() || stringAddress.isBlank() || stringRules.isBlank()))
            {
                val tournamentVal = Tournament(
                    tournamentName = tournamentNameField.text.toString(),
                    date = dateNameField.text.toString(),
                    time = timeNameField.text.toString(),
                    address = addressNameField.text.toString(),
                    rules = rulesField.text.toString(),
                    numberPlayers = numPlayerSpinner.selectedItem.toString(),
                    eventType = eventTypeSpinner.selectedItem.toString(),
                    isPrivate = currentRadioButton!!,
                    isMorning = false,
                    participants = participantsArr)
                Tournament.addTournamentToDatabase(tournamentVal)
                finish()
            }
        }
    }
}