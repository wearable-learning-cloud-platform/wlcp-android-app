package com.example.wearablelearning

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class StateSoundFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateContent = this.requireArguments().getString("content")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        val stateSound = this.requireArguments().getString("sound")
        val soundName = stateSound.toString().split(".")[0]
        val resID = resources.getIdentifier(soundName, "raw", context?.packageName)

        val mediaPlayer = MediaPlayer.create(context, resID)
        mediaPlayer.start()
    }
}