package com.example.wearablelearning

import android.os.Bundle
import android.util.Log
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
        Log.d("ID", id.toString())

        val skip = view.findViewById<Button>(R.id.skip_btn)
        val ret = view.findViewById<Button>(R.id.return_btn)

        //for this to work, the skip transition must be listed first and the return transition afterward
        skip.setOnClickListener {
            val skipID = id.toString().split(";;")[0]
            Log.d("SkipID", skipID)
            skipID?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "Skip") }
        }

        ret.setOnClickListener {
            val retID = id.toString().split(";;")[1]
            Log.d("retID", retID)
            retID?.let { it1 -> (activity as GameActivity).callTransition(it1, false, "Return") }
        }
    }
}
