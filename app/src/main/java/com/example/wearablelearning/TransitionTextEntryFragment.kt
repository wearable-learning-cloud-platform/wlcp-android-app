package com.example.wearablelearning

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class TransitionTextEntryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_text_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this.requireArguments().getString("id")

        val textEntryEditText = view.findViewById<EditText>(R.id.text_entry_edittext)

        val errorTextView = view.findViewById<TextView>(R.id.error_textview)

        textEntryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                errorTextView.visibility = TextView.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)

        submitButton.setOnClickListener {
            if(textEntryEditText.text.toString().lowercase() in getSolutions()) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1) }
            }
            else {
                errorTextView.visibility = TextView.VISIBLE
            }
        }

        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)

        clearButton.setOnClickListener {
            textEntryEditText.text.clear()
            errorTextView.visibility = TextView.INVISIBLE
        }
    }

    private fun getSolutions(): List<String> {
        val solution = this.requireArguments().getString("content")?.lowercase()

        return solution?.split(";") ?: listOf("")
    }
}
