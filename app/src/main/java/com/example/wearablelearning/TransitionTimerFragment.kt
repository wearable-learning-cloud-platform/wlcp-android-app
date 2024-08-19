package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.google.android.material.textview.MaterialTextView
import org.wlcp.wlcpgameserverapi.client.WLCPGameClient


class TransitionTimerFragment : Fragment() {

    private lateinit var wlcpGameClient: WLCPGameClient

    companion object {
        fun newInstance(): TransitionTimerFragment {
            return TransitionTimerFragment()
        }
    }

    fun setWLCPGameClient(wlcpGameClient: WLCPGameClient) {
        this.wlcpGameClient = wlcpGameClient
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        val id = this.requireArguments().getString("id")

        /** The _seconds_ is the current transition's duration. */
        val seconds = this.requireArguments().getString("content")

        /** The _frameLayout_ is the frame layout of [GameActivity]. */
        val frameLayout = this.requireArguments().getString("frameLayout")

        val timerLayout = view.findViewById<LinearLayout>(R.id.timer_layout)
        val padding1 = view.resources.getDimensionPixelSize(R.dimen.padding_side)
        val padding2 = view.resources.getDimensionPixelSize(R.dimen.transition_padding_bot)

        //Set the padding of _frameLayout_ depending if this fragment is replacing the blank fragment
        //in GameActivity.
        if(frameLayout!!.contains("2a")) {
            timerLayout.setPadding(padding1, padding1, padding1, padding1)
        }
        else {
            timerLayout.setPadding(padding1, padding1, padding1, padding2)
        }

        val milliseconds = seconds?.toLong()?.times(1000)?.plus(1000)
        val timerTextview = view.findViewById<MaterialTextView>(R.id.timer_textview)
        val timerText = getString(R.string.zero_seconds_text)
        val timerTextIntro = timerText.split(": ")[0]

        /**
         * The _timer_ displays the countdown in seconds. On finish, callTransition is called
         * in [GameActivity].
         */
        val timer = object: CountDownTimer(milliseconds!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = millisUntilFinished / 1000
                timerTextview.text = "$timerTextIntro: $remainingTime"
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                // Send the timer duration input to the backend API
                wlcpGameClient.sendRandomInput()
            }
        }
        timer.start()
    }
}
