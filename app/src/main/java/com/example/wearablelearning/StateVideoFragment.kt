package com.example.wearablelearning

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment

class StateVideoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateContent = this.requireArguments().getString("content")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        val stateVideo = this.requireArguments().getString("video")
        val videoName = stateVideo.toString().split(".")[0]
        val resID = resources.getIdentifier(videoName, "raw", context?.packageName)

        val videoView = getView()?.findViewById<VideoView>(R.id.state_videoView)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        if (videoView != null) {
            val path = "android.resource://" + context?.packageName + "/" + resID
            Log.d("path", path)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
        }
    }
}