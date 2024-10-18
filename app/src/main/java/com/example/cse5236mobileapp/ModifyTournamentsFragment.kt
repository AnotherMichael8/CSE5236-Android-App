package com.example.cse5236mobileapp

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class ModifyTournamentsFragment(tournamentIdentifier: TournamentIdentifier) : Fragment(R.layout.fragment_modify_tournaments){
    companion object {
        private const val TAG = "Modify Tournaments Fragment"
    }
    //subfragments
    private val modifyTextFragment = ModifyTextFragment()
    private val modifyBooleanFragment = ModifyBooleanFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //buttons
        val modifyTournamentNameButton = view.findViewById<Button>(R.id.modifyTournamentNameButton)
        val modifyNumberPlayersButton = view.findViewById<Button>(R.id.modifyNumberPlayersButton)
        val modifyEventTypeButton = view.findViewById<Button>(R.id.modifyEventTypeButton)
        val modifyDateButton = view.findViewById<Button>(R.id.modifyDateButton)
        val modifyTimeButton = view.findViewById<Button>(R.id.modifyTimeButton)
        val modifyAddressButton = view.findViewById<Button>(R.id.modifyAddressButton)
        val modifyIsPrivateButton = view.findViewById<Button>(R.id.modifyIsPrivateButton)
        val modifyIsMorningButton = view.findViewById<Button>(R.id.modifyIsMorningButton)
        val modifyRulesButton = view.findViewById<Button>(R.id.modifyRulesButton)

        //container
        val container = view.findViewById<FrameLayout>(R.id.modifierContainer)


        //tournament name button
        modifyTournamentNameButton.setOnClickListener(){
            changeContainerFragment(false, "Tournament Name: ", "Name", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify number players button
        modifyNumberPlayersButton.setOnClickListener(){
            changeContainerFragment(false, "Number Players: ", "Decimal Number", container,InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }
        //modify event type button
        modifyEventTypeButton.setOnClickListener(){
            changeContainerFragment(false, "Event Type:", "Event Type", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify date button
        modifyDateButton.setOnClickListener(){
            changeContainerFragment(false, "Date: ", "MM/DD/YY", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify time button
        modifyTimeButton.setOnClickListener(){
            changeContainerFragment(false, "Time: ", "HH:MM", container, InputType.TYPE_DATETIME_VARIATION_TIME)
        }
        //modify address button
        modifyAddressButton.setOnClickListener(){
            changeContainerFragment(false, "Address: ", "Address", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify is private button
        modifyIsPrivateButton.setOnClickListener(){
            changeContainerFragment(true, "Is Private: ", "None", container, InputType.TYPE_NULL)
        }
        //modify is morning button
        modifyIsMorningButton.setOnClickListener(){
            changeContainerFragment(true, "Is Morning: ", "None", container, InputType.TYPE_NULL)
        }
        //modify rules button
        modifyRulesButton.setOnClickListener(){
            changeContainerFragment(false, "Rules: ", "Rules: ", container, InputType.TYPE_CLASS_TEXT)
        }

        val backButton = view.findViewById<Button>(R.id.tournamentModifyBackButton)
        backButton.setOnClickListener(){
            parentFragmentManager.beginTransaction().replace(R.id.frgHomeScreenContainer, ViewTournamentsFragment()).commit()

        }

    }

    private fun changeContainerFragment(isBoolean: Boolean, newText: String, newHint: String, container: FrameLayout, inputType: Int){
        if(!isBoolean) {
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