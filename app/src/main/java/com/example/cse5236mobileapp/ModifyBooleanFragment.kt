package com.example.cse5236mobileapp

import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class ModifyBooleanFragment : Fragment(R.layout.fragment_modify_boolean) {
    companion object {
        private const val TAG = "Modify Boolean Fragment"
    }
    private lateinit var editBooleanText: TextView
    private lateinit var radioButton: RadioButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EditText Text
        editBooleanText = view.findViewById(R.id.modifyBooleanText)
        // Initialize EditText
        radioButton = view.findViewById<RadioButton>(R.id.modifyBooleanRadioButton)
    }
    fun updateRadioButtonText(newText: String){
        if(::radioButton.isInitialized) {
            radioButton.text = newText
        }
    }
    fun updateEditBooleanText(newText: String){
        if(::editBooleanText.isInitialized) {
            editBooleanText.text = newText
        }
    }
}