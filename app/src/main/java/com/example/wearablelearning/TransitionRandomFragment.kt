package com.example.wearablelearning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TransitionRandomFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_random, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var id = this.requireArguments().getString("id")

        val continueButton = view.findViewById<Button>(R.id.continue_btn)

        var gameInfo = (activity as GameActivity).gameInfo

        continueButton.setOnClickListener {
            gameInfo.interactionType = "continueBtnPress"

            var transitions = id?.split(";;")
            var transitionsCnt = transitions?.size
            var randomIdx = (0 until transitionsCnt!!).random()

            id = transitions?.get(randomIdx)

            id?.let { transId -> (activity as GameActivity).callTransition(transId, false, "", "random") }
        }
    }
}
