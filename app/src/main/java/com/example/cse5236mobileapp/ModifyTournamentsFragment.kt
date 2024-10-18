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

class ModifyTournamentsFragment : Fragment(R.layout.fragment_modify_tournaments){
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
            changeFragment(false, "@string/modify_tournament_name_text", "Name", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify number players button
        modifyNumberPlayersButton.setOnClickListener(){
            changeFragment(false, "@string/modify_number_players_text", "Decimal Number", container,InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }
        //modify event type button
        modifyEventTypeButton.setOnClickListener(){
            changeFragment(false, "@string/modify_event_type_text", "@string/modify_event_type_text", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify date button
        modifyDateButton.setOnClickListener(){
            changeFragment(false, "@string/modify_date_text", "MM/DD/YY", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify time button
        modifyTimeButton.setOnClickListener(){
            changeFragment(false, "@string/modify_time_text", "HH:MM", container, InputType.TYPE_DATETIME_VARIATION_TIME)
        }
        //modify address button
        modifyAddressButton.setOnClickListener(){
            changeFragment(false, "@string/modify_address_text", "@string/modify_address_text", container, InputType.TYPE_CLASS_TEXT)
        }
        //modify is private button
        modifyIsPrivateButton.setOnClickListener(){
            changeFragment(true, "@string/modify_is_private_text", "None", container, InputType.TYPE_NULL)
        }
        //modify is morning button
        modifyIsMorningButton.setOnClickListener(){
            changeFragment(true, "@string/modify_is_morning_text", "None", container, InputType.TYPE_NULL)
        }
        //modify rules button
        modifyRulesButton.setOnClickListener(){
            changeFragment(false, "@string/modify_rules_text", "@string/modify_rules_text", container, InputType.TYPE_CLASS_TEXT)
        }

    }

    private fun changeFragment(isBoolean: Boolean, newText: String, newHint: String, container: FrameLayout, inputType: Int){
        removeFragmentIfExists(container.id)
        if(!isBoolean) {
            parentFragmentManager.beginTransaction()
                .add(R.id.frgHomeScreenContainer, modifyTextFragment).commit()
            modifyTextFragment.updateEditTextText("Modify " + newText)
            modifyTextFragment.updateEditTextHintAndInputType(newHint, inputType)
        } else {
            parentFragmentManager.beginTransaction()
                .add(R.id.frgHomeScreenContainer, modifyBooleanFragment).commit()
            modifyBooleanFragment.updateEditBooleanText("Modify " + newText)
            modifyBooleanFragment.updateRadioButtonText(newText)
        }
    }

    private fun removeFragmentIfExists(containerId: Int){
        // Get the FragmentManager
        val fragmentManager = parentFragmentManager

        // Check if there's a fragment in the given FrameLayout (containerId)
        val fragment = fragmentManager.findFragmentById(containerId)

        if (fragment != null) {
            // Fragment exists, so remove it
            fragmentManager.beginTransaction().remove(fragment).commit()
            Log.d("FragmentCheck", "Fragment removed from FrameLayout.")
        } else {
            // Fragment does not exist
            Log.d("FragmentCheck", "No fragment found in FrameLayout.")
        }
    }


}