package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TransitionRetOrSkipFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_ret_or_skip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this.requireArguments().getString("id")
        val solution = this.requireArguments().getString("content")

        val skip = view.findViewById<Button>(R.id.skip_btn)
        val ret = view.findViewById<Button>(R.id.ret_btn)

        skip.setOnClickListener {
            if(solution != null && solution.contains("Skip")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "RD") }
            }
        }

        ret.setOnClickListener {
            if(solution != null && solution.contains("Return")) {
                id?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "GR") }
            }
        }
    }
}
