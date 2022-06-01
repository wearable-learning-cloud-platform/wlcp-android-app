package com.example.wearablelearning

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        var frameLayout = this.requireArguments().getString("frameLayout")

        val timerLayout = view.findViewById<LinearLayout>(R.id.timer_layout)
        val padding1 = view.resources.getDimensionPixelSize(R.dimen.padding_side)
        val padding2 = view.resources.getDimensionPixelSize(R.dimen.transition_padding_bot)

        if(frameLayout!!.contains("2a")) {
            timerLayout.setPadding(padding1, padding1, padding1, padding1)
        }
        else {
            timerLayout.setPadding(padding1, padding1, padding1, padding2)
        }

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
                var gameInfo = (activity as? GameActivity)?.gameInfo
                gameInfo?.interactionType = seconds.plus("s timer expired")
                id?.let { transId -> (activity as? GameActivity)?.callTransition(transId, false, seconds.plus("s timer"), seconds.plus("s timer")) }
            }
        }
        timer.start()
    }
}
