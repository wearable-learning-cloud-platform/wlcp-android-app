package com.example.wearablelearning

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

/**
 * The [StatePhotoAndSoundFragment] class is called by [GameActivity]. This fragment holds a photo
 * and sound.
 */
class StatePhotoAndSoundFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_photo_and_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /** Retrieve the photo name from the bundle and set _state_imageView_ to the photo resource. */
        val stateImage = this.requireArguments().getString("photo")
        val imageName = stateImage.toString().split(".")[0]
        val resIDImage = resources.getIdentifier(imageName, "raw", context?.packageName)
        getView()?.findViewById<ImageView>(R.id.state_imageView)?.setImageResource(resIDImage)

        /** Retrieve the sound name from the bundle and set the media player to the sound resource. */
        val stateSound = this.requireArguments().getString("sound")
        val soundName = stateSound.toString().split(".")[0]
        val resIDSound = resources.getIdentifier(soundName, "raw", context?.packageName)

        StateSoundFragment.mediaPlayer = MediaPlayer.create(context, resIDSound)

        /** The rewind button for the media player that rewinds the sound to a max of 10 seconds. */
        val rewindButton = view.findViewById<MaterialButton>(R.id.rewind_btn)

        rewindButton.setOnClickListener {
            var currPos = StateSoundFragment.mediaPlayer.currentPosition

            if(currPos < 0) {
                currPos = 0
            }

            StateSoundFragment.mediaPlayer.seekTo(currPos - 10000)
        }

        /** The play button for the media player that plays the sound from where it left off. */
        val playButton = view.findViewById<MaterialButton>(R.id.play_btn)

        playButton.setOnClickListener {
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

        /** Resets the media player on completion. */
        StateSoundFragment.mediaPlayer.setOnCompletionListener {
            StateSoundFragment.mediaPlayer.seekTo(0)
            StateSoundFragment.isPaused = true
            playButton.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
        }
    }

    /**
     * The [onPause] function stops playing sound if the sound was playing form the media player.
     */
    override fun onPause() {
        super.onPause()

        if(StateSoundFragment.mediaPlayer.isPlaying) {
            StateSoundFragment.mediaPlayer.stop()
        }

        StateSoundFragment.mediaPlayer.reset()
        StateSoundFragment.mediaPlayer.release()
    }
}