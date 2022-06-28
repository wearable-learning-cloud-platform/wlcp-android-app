package com.example.wearablelearning

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


class StateVideoFragment : Fragment() {
    lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_video, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateContent = this.requireArguments().getString("text")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        val stateVideo = this.requireArguments().getString("video")
        val videoName = stateVideo.toString().split(".")[0]
        val resID = resources.getIdentifier(videoName, "raw", context?.packageName)

        videoView = requireView().findViewById(R.id.state_videoView)!!
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        mediaController.visibility = View.GONE
        videoView?.seekTo(1)

        var videoPath = ""
        var videoOrientation = ""

        if (videoView != null) {
            videoPath = "android.resource://" + context?.packageName + "/" + resID
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(videoPath))


        }

        videoView.setOnPreparedListener(OnPreparedListener { mp ->
            mp.setOnVideoSizeChangedListener { mp, width, height ->
                val width: Int = videoView.width
                val height: Int = videoView.height

                videoOrientation = if(width >= height) {
                    "landscape"
                } else {
                    "portrait"
                }
            }
        })

        videoView?.setOnTouchListener(OnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val intent = Intent(activity, VideoFullscreenActivity::class.java)
                    intent.putExtra("videoPath", videoPath)
                    intent.putExtra("videoOrientation", videoOrientation)
                    startActivity(intent)
                }
            }

            v?.onTouchEvent(event) ?: true
        })

        getView()?.findViewById<MaterialButton>(R.id.play_btn)?.setOnClickListener {
            val motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_DOWN,
                android.R.attr.x.toFloat(),
                android.R.attr.y.toFloat(),
                0
            )

            videoView?.dispatchTouchEvent(motionEvent)
        }
    }

    override fun onResume() {
        super.onResume()

        videoView.seekTo(1)
    }
}