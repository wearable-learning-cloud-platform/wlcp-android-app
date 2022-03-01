package com.example.wearablelearning

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment


class StateVideoFragment : Fragment() {
    companion object {
        var isPaused = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_video, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateContent = this.requireArguments().getString("content")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        val stateVideo = this.requireArguments().getString("video")
        val videoName = stateVideo.toString().split(".")[0]
        val resID = resources.getIdentifier(videoName, "raw", context?.packageName)

        val videoView = getView()?.findViewById<VideoView>(R.id.state_videoView)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        mediaController.visibility = View.GONE

        if (videoView != null) {
            val path = "android.resource://" + context?.packageName + "/" + resID
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(path))
            videoView.seekTo(1)
        }

        videoView?.setOnTouchListener(OnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(isPaused) {
                        isPaused = false

                        //TODO expand video to fullscreen
//                        val metrics = requireContext().resources.displayMetrics
//                        val width = metrics.widthPixels
//                        val height = metrics.heightPixels
//
//                        videoView.layoutParams = FrameLayout.LayoutParams(width, height)

                        videoView.start()
                    }
                    else {
                        isPaused = true
                        videoView.pause()
                    }

                }
            }

            v?.onTouchEvent(event) ?: true
        })
    }
}