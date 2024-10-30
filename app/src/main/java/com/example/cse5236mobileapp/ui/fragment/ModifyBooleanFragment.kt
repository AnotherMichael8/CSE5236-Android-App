package com.example.cse5236mobileapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cse5236mobileapp.R

class ModifyBooleanFragment(private var modifyTournamentsFragmentParent: ModifyTournamentsFragment) : Fragment(
    R.layout.fragment_modify_boolean
) {
    companion object {
        private const val TAG = "Modify Boolean Fragment"
    }
    private lateinit var editBooleanText: TextView
    private lateinit var checkBox: CheckBox

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EditText Text
        editBooleanText = view.findViewById(R.id.modifyBooleanText)
        // Initialize EditText
        checkBox = view.findViewById<CheckBox>(R.id.modifyBooleanCheckBox)
        checkBox.setOnClickListener(){
            modifyTournamentsFragmentParent.updatedBoolean = checkBox.isChecked
        }

    }
    fun updateRadioButtonText(newText: String){
        if(::checkBox.isInitialized) {
            checkBox.text = newText
        }
    }
    fun updateEditBooleanText(newText: String){
        if(::editBooleanText.isInitialized) {
            editBooleanText.text = newText
        }
    }
}