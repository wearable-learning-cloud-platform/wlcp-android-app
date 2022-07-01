package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi

class TransitionRandomFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_random, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        var id = this.requireArguments().getString("id")

        val continueButton = view.findViewById<Button>(R.id.continue_btn)

        /**
         * Retrieve the [GameInfo] object from the intent that started [GameActivity].
         *
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         */
        val gameInfo = (activity as GameActivity).gameInfo

        /**
         * If only button in fragment is selected, then callTransition in [GameActivity] is called
         * on a random transition selected from a list of possible transitions listed in the
         * json.
         */
        continueButton.setOnClickListener {
            gameInfo.interactionType = "continueBtnPress"

            val transitions = id?.split(";;")
            val transitionsCnt = transitions?.size
            val randomIdx = (0 until transitionsCnt!!).random()

            id = transitions[randomIdx]

            id?.let { transId -> (activity as GameActivity).callTransition(transId, false, "", "random") }
        }
    }
}
