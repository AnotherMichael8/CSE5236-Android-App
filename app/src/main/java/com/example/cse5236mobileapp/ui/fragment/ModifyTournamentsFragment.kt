package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.cse5236mobileapp.R
import com.example.cse5236mobileapp.model.TournamentIdentifier
import com.example.cse5236mobileapp.model.Tournament
import com.example.cse5236mobileapp.model.TournamentUser
import com.example.cse5236mobileapp.model.viewmodel.TournamentUserViewModel
import com.example.cse5236mobileapp.model.viewmodel.TournamentViewModel

class ModifyTournamentsFragment(private val tournamentIdentifier: TournamentIdentifier) :
    Fragment(R.layout.fragment_modify_tournaments) {
    companion object {
        private const val TAG = "Modify Tournaments Fragment"
    }

    // Viewmodel object
    private val tournamentViewModel = TournamentViewModel()
    private val tournamentUserViewModel = TournamentUserViewModel()

    //subfragments
    private val modifyTextFragment = ModifyTextFragment(this)
    private val modifyBooleanFragment = ModifyBooleanFragment(this)

    // if any changes are made it should update into these variables
    var updatedBoolean = false
    var updatedText = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //buttons
        val modifyTournamentNameButton = view.findViewById<Button>(R.id.modifyTournamentNameButton)
        val modifyNumberPlayersButton = view.findViewById<Button>(R.id.modifyNumberPlayersButton)
        val modifyDateButton = view.findViewById<Button>(R.id.modifyDateButton)
        val modifyTimeButton = view.findViewById<Button>(R.id.modifyTimeButton)
        val modifyAddressButton = view.findViewById<Button>(R.id.modifyAddressButton)
        val modifyIsPrivateButton = view.findViewById<Button>(R.id.modifyIsPrivateButton)
        val modifyIsMorningButton = view.findViewById<Button>(R.id.modifyIsMorningButton)
        val modifyRulesButton = view.findViewById<Button>(R.id.modifyRulesButton)

        //container
        val container = view.findViewById<FrameLayout>(R.id.modifierContainer)

        // var to keep track of which property is selected
        var selectedProperty = ""


        //tournament name button
        modifyTournamentNameButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Tournament Name: ",
                "Name",
                container,
                InputType.TYPE_CLASS_TEXT
            )
            selectedProperty = "TournamentName"
        }
        //modify number players button
        modifyNumberPlayersButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Number Players: ",
                "Decimal Number",
                container,
                InputType.TYPE_NUMBER_FLAG_DECIMAL
            )
            selectedProperty = "NumberPlayers"
        }
        //modify date button
        modifyDateButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Date: ",
                "MM/DD/YY",
                container,
                InputType.TYPE_CLASS_TEXT
            )
            selectedProperty = "Date"
        }
        //modify time button
        modifyTimeButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Time: ",
                "HH:MM",
                container,
                InputType.TYPE_DATETIME_VARIATION_TIME
            )
            selectedProperty = "Time"
        }
        //modify address button
        modifyAddressButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Address: ",
                "Address",
                container,
                InputType.TYPE_CLASS_TEXT
            )
            selectedProperty = "Address"
        }
        //modify is private button
        modifyIsPrivateButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedBoolean = false
            changeContainerFragment(true, "Is Private: ", "None", container, InputType.TYPE_NULL)
            selectedProperty = "isPrivate"
        }
        //modify is morning button
        modifyIsMorningButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedBoolean = false
            changeContainerFragment(true, "Is Morning: ", "None", container, InputType.TYPE_NULL)
            selectedProperty = "isMorning"
        }
        //modify rules button
        modifyRulesButton.setOnClickListener() {
            Log.d(TAG, "Modifying: " + tournamentIdentifier.tournamentId)
            updatedText = ""
            changeContainerFragment(
                false,
                "Rules: ",
                "Rules: ",
                container,
                InputType.TYPE_CLASS_TEXT
            )
            selectedProperty = "Rules"
        }

        val backButton = view.findViewById<Button>(R.id.tournamentModifyBackButton)
        backButton.setOnClickListener() {
            parentFragmentManager.popBackStack()
        }

        val submitButton = view.findViewById<Button>(R.id.modifyTournamentsSubmitButton)
        submitButton.setOnClickListener() {
            if (selectedProperty != "") {
                if (selectedProperty != "isMorning" && selectedProperty != "isPrivate") {
                    tournamentViewModel.modifyTournamentAttribute(
                        tournamentIdentifier,
                        selectedProperty,
                        updatedText
                    )
                } else {
                    tournamentViewModel.modifyTournamentAttribute(
                        tournamentIdentifier,
                        selectedProperty,
                        updatedBoolean
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No Property Selected",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        val deleteButton = view.findViewById<Button>(R.id.deleteTournamentButton)
        deleteButton.setOnClickListener() {

            // Removing fragment stack first because previous fragments rely on deleted data
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)


            Handler(Looper.getMainLooper()).postDelayed({
                // Removing tournament
                tournamentViewModel.deleteTournament(tournamentIdentifier)
                // Remove tournament for corresponding players
                tournamentUserViewModel.removeTournamentForAllUsers(
                    tournamentIdentifier.tournamentId,
                    tournamentIdentifier.tournament.players
                )
            }, 1000)
        }
    }

    private fun changeContainerFragment(
        isBoolean: Boolean,
        newText: String,
        newHint: String,
        container: FrameLayout,
        inputType: Int
    ) {

        if (!isBoolean) {
            modifyTextFragment.updateEditTextText("Modify " + newText)
            modifyTextFragment.updateEditTextHintAndInputType(newHint, inputType)
            parentFragmentManager.beginTransaction()
                .replace(R.id.modifierContainer, modifyTextFragment).commit()
        } else {
            modifyBooleanFragment.updateEditBooleanText("Modify " + newText)
            modifyBooleanFragment.updateRadioButtonText(newText)
            parentFragmentManager.beginTransaction()
                .replace(R.id.modifierContainer, modifyBooleanFragment).commit()
        }
    }

}