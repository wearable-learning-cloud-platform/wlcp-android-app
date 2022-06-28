package com.example.wearablelearning

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class StateSoundFragment : Fragment() {
    companion object {
        var mediaPlayer = MediaPlayer()
        var isPaused = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateContent = this.requireArguments().getString("text")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        val stateSound = this.requireArguments().getString("sound")
        val soundName = stateSound.toString().split(".")[0]
        val resID = resources.getIdentifier(soundName, "raw", context?.packageName)

        mediaPlayer = MediaPlayer.create(context, resID)

        val rewindButton = view.findViewById<MaterialButton>(R.id.rewind_btn)

        rewindButton.setOnClickListener { v ->
            var currPos = mediaPlayer.currentPosition

            if(currPos < 0) {
                currPos = 0
            }

            mediaPlayer.seekTo(currPos - 10000)
        }

        val playButton = view.findViewById<MaterialButton>(R.id.play_btn)

        playButton.setOnClickListener { v ->
            if(isPaused) {
                isPaused = false
                mediaPlayer.start()

                playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_pause_foreground)}
            }
            else {
                isPaused = true

                if(mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }

                playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
            }
        }

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            isPaused = true
            playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
        }
    }

    override fun onPause() {
        super.onPause()

        if(mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        mediaPlayer.reset()
        mediaPlayer.release()
    }
}