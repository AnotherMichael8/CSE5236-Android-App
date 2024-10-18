package com.example.cse5236mobileapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class ModifyTextFragment(private var modifyTournamentsFragment: ModifyTournamentsFragment) : Fragment(R.layout.fragment_modify_text) {
    companion object {
        private const val TAG = "ModifyTextFragment"
    }

    private lateinit var editText: EditText
    private lateinit var editTextText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EditText
        editText = view.findViewById<EditText>(R.id.modifyTextEditText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // You can leave this empty if you don't need it
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                modifyTournamentsFragment.updatedText = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
                // You can leave this empty if you don't need it
            }
        })

        // Initialize EditText Text
        editTextText = view.findViewById<TextView>(R.id.modifyBooleanText)
    }

    fun updateEditTextHintAndInputType(newHint: String, inputType: Int){
        if(::editText.isInitialized) {
            editText.hint = newHint
            // You can call the method and pass the input type that you want (e.g., InputType.TYPE_CLASS_TEXT or any variation like InputType.TYPE_TEXT_VARIATION_PASSWORD)
            editText.inputType = inputType
        }
    }
    fun updateEditTextText(newText: String){
        if(::editTextText.isInitialized) {
            editTextText.text = newText
        }
    }

}