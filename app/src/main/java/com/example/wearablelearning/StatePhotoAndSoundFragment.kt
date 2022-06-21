package com.example.wearablelearning

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class StatePhotoAndSoundFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_photo_and_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //image
        val stateImage = this.requireArguments().getString("image")
        val imageName = stateImage.toString().split(".")[0]
        val resIDImage = resources.getIdentifier(imageName, "raw", context?.packageName);
        getView()?.findViewById<ImageView>(R.id.state_imageView)?.setImageResource(resIDImage)

        //sound
        val stateSound = this.requireArguments().getString("sound")

        val soundName = stateSound.toString().split(".")[0]
        val resIDSound = resources.getIdentifier(soundName, "raw", context?.packageName)

        StateSoundFragment.mediaPlayer = MediaPlayer.create(context, resIDSound)

        val rewindButton = view.findViewById<MaterialButton>(R.id.rewind_btn)

        rewindButton.setOnClickListener { v ->
            var currPos = StateSoundFragment.mediaPlayer.currentPosition

            if(currPos < 0) {
                currPos = 0
            }

            StateSoundFragment.mediaPlayer.seekTo(currPos - 10000)
        }

        val playButton = view.findViewById<MaterialButton>(R.id.play_btn)

        playButton.setOnClickListener { v ->
            if(StateSoundFragment.isPaused) {
                StateSoundFragment.isPaused = false
                StateSoundFragment.mediaPlayer.start()

                playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_pause_foreground)}
            }
            else {
                StateSoundFragment.isPaused = true

                if(StateSoundFragment.mediaPlayer.isPlaying) {
                    StateSoundFragment.mediaPlayer.pause()
                }

                playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
            }
        }

        StateSoundFragment.mediaPlayer?.setOnCompletionListener {
            StateSoundFragment.mediaPlayer.seekTo(0)
            StateSoundFragment.isPaused = true
            playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
        }
    }

    override fun onPause() {
        super.onPause()

        if(StateSoundFragment.mediaPlayer.isPlaying) {
            StateSoundFragment.mediaPlayer.stop()
        }

        StateSoundFragment.mediaPlayer.reset()
        StateSoundFragment.mediaPlayer.release()
    }
}