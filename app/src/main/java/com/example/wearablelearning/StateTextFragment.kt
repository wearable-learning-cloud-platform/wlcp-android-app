package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * The [StateTextFragment] class is called by [GameActivity]. This fragment holds a text.
 */
class StateTextFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /** Retrieve the text to be displayed in the textview. */
        val stateContent = this.requireArguments().getString("text")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent
    }
}