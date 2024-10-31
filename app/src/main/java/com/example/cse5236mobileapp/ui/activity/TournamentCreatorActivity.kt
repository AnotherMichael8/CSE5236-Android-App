package com.example.cse5236mobileapp.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.Game
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

class TournamentCreatorActivity : AppCompatActivity() {

    private val tournamentViewModel = TournamentViewModel()

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
//                // TODO: Have method for generating list of games be in tournament class
//                var gameListID = mutableListOf<String>()
//                var gameList = mutableListOf<Game>()
//                for(i in 0 until participantsArr.size/2) {
//                    val playersPos = i * 2
//                    val game = Game(
//                        teamOne = participantsArr[playersPos],
//                        teamTwo = participantsArr[playersPos + 1]
//                    )
//                    gameList.add(game)
//                    gameListID.add(Game.createGame(game))
//                }
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
                    players = participantsArr)

                tournamentVal.createInitialGames()

                tournamentViewModel.addTournament(tournamentVal)
                finish()
            }
        }
    }
}