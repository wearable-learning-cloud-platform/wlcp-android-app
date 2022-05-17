package com.example.wearablelearning

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textview.MaterialTextView

class TransitionTimerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var id = this.requireArguments().getString("id")
        var seconds = this.requireArguments().getString("content")
        var milliseconds = seconds?.toLong()?.times(1000)?.plus(1000)

        val timerTextview = view.findViewById<MaterialTextView>(R.id.timer_textview)

        var timerText = getString(R.string.zero_seconds_text)
        var timerTextIntro = timerText.split(": ")[0]

        val timer = object: CountDownTimer(milliseconds!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = millisUntilFinished / 1000
                timerTextview.text = "$timerTextIntro: $remainingTime"
            }

            override fun onFinish() {
                id?.let { transId -> (activity as GameActivity).callTransition(transId, false, "", "timer") }
            }
        }
        timer.start()
    }
}
