package com.example.wearablelearning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TransitionBtnPressFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_btn_press, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this.requireArguments().getString("id")
        val solution = this.requireArguments().getString("content")

        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        var gameInfo = (activity as GameActivity).gameInfo

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
