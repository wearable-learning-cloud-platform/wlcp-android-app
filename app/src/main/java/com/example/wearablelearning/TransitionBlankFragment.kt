package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * The [TransitionBlankFragment] class is called by [GameActivity]. This fragment is blank and is
 * used when there is only one transition fragment. This fragment allows for no user actions and
 * is simply a place holder in [GameActivity].
 */
class TransitionBlankFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_blank, container, false)
    }
}
