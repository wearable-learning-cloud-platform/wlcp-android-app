package com.example.wearablelearning

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat

class TransitionSequenceFragment : Fragment() {
    companion object {
        const val maxBlocks = 8
        private var colorBtnClicks = 0
        private var colorSequence: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_sequence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this.requireArguments().getString("id")
        val solution = this.requireArguments().getString("content")

        val submitButton = view.findViewById<Button>(R.id.transition_submit_btn)
        val clearButton = view.findViewById<Button>(R.id.transition_clear_btn)
        val redButton = view.findViewById<Button>(R.id.question_red_btn)
        val greenButton = view.findViewById<Button>(R.id.question_green_btn)
        val blueButton = view.findViewById<Button>(R.id.question_blue_btn)
        val blackButton = view.findViewById<Button>(R.id.question_black_btn)

        submitButton.setOnClickListener { v ->
            if(colorSequence == solution) {
                colorBtnClicks = 0
                colorSequence = ""
                id?.let { it1 -> (activity as GameActivity).callTransition(it1) }
            }
        }

        clearButton.setOnClickListener {
            for(i in 1..maxBlocks) {
                val blockId = resources.getIdentifier(
                    "question_temp_btn_$i",
                    "id",
                    requireContext().packageName
                )

                view.findViewById<Button>(blockId).setBackgroundColor(Color.TRANSPARENT)
            }

            colorBtnClicks = 0
            colorSequence = ""
        }

        redButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(greenButton, R.color.red, view)
            addKeywordToSequence("RD")
        }

        greenButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(greenButton, R.color.green, view)
            addKeywordToSequence("GR")
        }

        blueButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(blueButton, R.color.blue, view)
            addKeywordToSequence("BL")
        }

        blackButton.setOnClickListener {
            colorBtnClicks++
            addBlockToSolution(blackButton, R.color.black, view)
            addKeywordToSequence("BK")
        }
    }

    private fun addBlockToSolution(clickedButton: Button, color: Int, view: View) {
        if (colorBtnClicks <= maxBlocks) {
            val buttonId = resources.getIdentifier(
                "question_temp_btn_$colorBtnClicks",
                "id",
                requireContext().packageName
            )
            view.findViewById<Button>(buttonId)
                .setBackgroundColor(ContextCompat.getColor(requireContext(), color))
        }
    }

    private fun addKeywordToSequence(keyword: String) {
        if(colorSequence.isEmpty()) {
            colorSequence = keyword
        } else {
            colorSequence = "$colorSequence&$keyword"
        }
    }
}
