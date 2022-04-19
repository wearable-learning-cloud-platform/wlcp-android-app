package com.example.wearablelearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.registerEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


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
        val content = this.requireArguments().getString("content")

        val textEntryEditText = view.findViewById<EditText>(R.id.text_entry_edittext)

        val errorTextView = view.findViewById<TextView>(R.id.error_textview)

        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)

        submitButton.setOnClickListener {
            val input = textEntryEditText.text.toString()

            //one possible transition and correct input
            if(!id.toString().contains(";;") && checkInput(input.lowercase(), content.toString())) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, input) }
            }
            //one possible transition and incorrect input
            else if(!id.toString().contains(";;")) {
                errorTextView.visibility = TextView.VISIBLE
            }
            //multiple possible transitions
            else {
                val matchedId = checkInputOnMultipleTransitions(input.lowercase(), content.toString(), id.toString())

                if(!StringUtils.isEmptyOrBlank(matchedId)) {
                    matchedId?.let { it1 -> (activity as GameActivity).callTransition(it1, false, input) }
                }
            }
        }

        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)

        clearButton.setOnClickListener {
            textEntryEditText.text.clear()
            errorTextView.visibility = TextView.INVISIBLE
        }

        setEventListener(
            requireActivity(),
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    val clearAndSubmitBtnGroup = view.findViewById<LinearLayout>(R.id.clear_and_submit_btns)
                    val padding1 = view.resources.getDimensionPixelSize(R.dimen.padding_side)
                    val padding2 = view.resources.getDimensionPixelSize(R.dimen.transition_padding_bot)

                    if(isOpen) {
                        clearAndSubmitBtnGroup.setPadding(padding1, padding1, padding1, padding1)
                    }
                    else {
                        clearAndSubmitBtnGroup.setPadding(padding1, padding1, padding1, padding2)
                    }
                }
            })
    }

    private fun checkInput(input: String, content:String): Boolean {
        val solutionStr = content?.lowercase()
        val solutionsList = solutionStr?.split(";") ?: listOf("")

        if(content == "ALL_OTHER_INPUTS") {
            return true
        }

        return input in solutionsList
    }

    private fun checkInputOnMultipleTransitions(input: String, content: String, id: String): String {
        val ids = id.split(";;")
        val contents = content.split(";;")

        if(ids.size == contents.size) {
            ids.zip(contents).forEach {(i, c) ->
                if(checkInput(input, c)) {
                    return i
                }
            }
        }

        return ""
    }

    fun spToPx(sp: Float): Float {
        return sp * resources.displayMetrics.scaledDensity
    }
}
