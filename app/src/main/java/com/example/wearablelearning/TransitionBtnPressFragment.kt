package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi

/**
 * The [TransitionBtnPressFragment] class is called by [GameActivity]. This fragment includes 4
 * buttons (red, green, blue, and black). The user is expected to click one of these 4 buttons.
 */
class TransitionBtnPressFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_btn_press, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        val id = this.requireArguments().getString("id")

        /** The _solution_ is the current transition's correct answer (i.e. expected user input). */
        val solution = this.requireArguments().getString("content")

        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        /**
         * Retrieve the [GameInfo] object from the intent that started [GameActivity].
         *
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         */
        val gameInfo = (activity as GameActivity).gameInfo

        /**
         * If RED BUTTON is clicked and the solution is RD then call callTransition in [GameActivity].
         * Otherwise, user is unsuccessful in moving on.
         */
        redButton.setOnClickListener {
            gameInfo.interactionType = "coloredButtonPress"

            if(solution != null && solution.contains("RD")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "RD", "button_press") }
            }
            else {
                gameInfo.currTransAnswer = "RD"
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
        }

        /**
         * If GREEN BUTTON is clicked and the solution is GR then call callTransition in [GameActivity].
         * Otherwise, user is unsuccessful in moving on.
         */
        greenButton.setOnClickListener {
            gameInfo.interactionType = "coloredButtonPress"

            if(solution != null && solution.contains("GR")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "GR", "button_press") }
            }
            else {
                gameInfo.currTransAnswer = "GR"
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
        }

        /**
         * If BLUE BUTTON is clicked and the solution is BL then call callTransition in [GameActivity].
         * Otherwise, user is unsuccessful in moving on.
         */
        blueButton.setOnClickListener {
            gameInfo.interactionType = "coloredButtonPress"

            if(solution != null && solution.contains("BL")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "BL", "button_press") }
            }
            else {
                gameInfo.currTransAnswer = "BL"
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
        }

        /**
         * If BLACK BUTTON is clicked and the solution is BK then call callTransition in [GameActivity].
         * Otherwise, user is unsuccessful in moving on.
         */
        blackButton.setOnClickListener {
            gameInfo.interactionType = "coloredButtonPress"

            if(solution != null && solution.contains("BK")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "BK", "button_press") }
            }
            else {
                gameInfo.currTransAnswer = "BK"
                context?.let { context -> LogUtils.logGamePlay("player", (activity as GameActivity).gameInfo, false, context) }
                context?.let { context -> LogUtils.logGamePlay("gamePlay", (activity as GameActivity).gameInfo, false, context) }
            }
        }
    }
}
