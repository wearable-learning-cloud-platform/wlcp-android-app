package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * The [TransitionBlankFragment] class is called by [GameActivity]. This fragment is blank and is
 * used when there is only one transition fragment. The only time this fragment is not used is if
 * there are more than one transition fragments (e.g. timer and button press). This fragment allows
 * for no user actions and is simple a place holder.
 */
class TransitionBlankFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_blank, container, false)
    }
}
