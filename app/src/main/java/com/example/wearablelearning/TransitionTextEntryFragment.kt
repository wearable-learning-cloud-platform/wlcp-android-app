package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class TransitionTextEntryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_text_entry, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        val id = this.requireArguments().getString("id")

        /** The _content_ is the current transition's correct answer (i.e. expected user input). */
        val content = this.requireArguments().getString("content")

        val textEntryEditText = view.findViewById<EditText>(R.id.text_entry_edittext)
        val errorTextView = view.findViewById<TextView>(R.id.error_textview)

        /**
         * The _submitButton_ checks the user's input to the correct solution _content_.
         */
        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)

        submitButton.setOnClickListener {
            val input = textEntryEditText.text.toString()

            /**
             * Retrieve the [GameInfo] object from the intent that started [GameActivity].
             *
             * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
             * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
             */
            val gameInfo = (activity as GameActivity).gameInfo
            gameInfo.interactionType = "submitButton"

            //one possible transition and correct input
            if(!id.toString().contains(";;") && checkInput(input.lowercase(), content.toString())) {
                gameInfo.prevTransAnswer = "text_entry"
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, input, "text_entry") }
            }
            //one possible transition and incorrect input
            else if(!id.toString().contains(";;")) {
                errorTextView.visibility = TextView.VISIBLE

                gameInfo.currTransAnswer = input.lowercase()
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
            //multiple possible transitions
            else {
                val matchedId = checkInputOnMultipleTransitions(input.lowercase(), content.toString(), id.toString())

                if(!StringUtils.isEmptyOrBlank(matchedId)) {
                    gameInfo.prevTransAnswer = "text_entry"
                    matchedId.let { it1 -> (activity as GameActivity).callTransition(it1, false, input, "text_entry") }
                }
                else {
                    val gameInfo = (activity as GameActivity).gameInfo
                    gameInfo.currTransAnswer = input.lowercase()
                    context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                    context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
                }
            }
        }

        /**
         * The _clearButton_ deletes the user's input (after a confirmation dialog is approved).
         */
        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)

        clearButton.setOnClickListener {
            context?.let { context1 ->
                MaterialAlertDialogBuilder(context1, R.style.Theme_WearableLearning_AlertDialog)
                    .setMessage(resources.getString(R.string.confirm_clear_text))
                    .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                        /**
                         * Retrieve the [GameInfo] object from the intent that started [GameActivity].
                         *
                         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
                         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
                         */
                        val gameInfo = (activity as GameActivity).gameInfo

                        gameInfo.interactionType = "clearButton"
                        gameInfo.currTransAnswer = textEntryEditText.text.toString()

                        textEntryEditText.text.clear()
                        errorTextView.visibility = TextView.INVISIBLE

                        context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                        context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
                    }
                    .show()
            }
        }

        /**
         * When the user types, the keyboard takes up screen space. To combat this, the padding
         * under the text box is decreased when the keyboard is visible.
         */
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

    /**
     * The [checkInput] function compares the user's answer to the correct answer on submit.
     * @param [input] The user's input.
     * @param [content] The solution.
     */
    private fun checkInput(input: String, content:String): Boolean {
        val solutionStr = content.lowercase()
        val solutionsList = solutionStr.split(";") ?: listOf("")

        if(content == "ALL_OTHER_INPUTS") {
            return true
        }

        return input in solutionsList
    }

    /**
     * The [checkInputOnMultipleTransitions] function calls [checkInput] for each possible output
     * transition.
     * @param [input] The user's input.
     * @param [content] The solution.
     * @param [id] The id of the transition.
     */
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

//    fun spToPx(sp: Float): Float {
//        return sp * resources.displayMetrics.scaledDensity
//    }
}
