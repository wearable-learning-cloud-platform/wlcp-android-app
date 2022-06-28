package com.example.wearablelearning

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

/**
 * The [StateVideoFragment] class is called by [GameActivity]. This fragment holds a video.
 */
class StateVideoFragment : Fragment() {
    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_video, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /** Retrieve the text from the bundle. This may be null/empty in which case no text is displayed. */
        val stateContent = this.requireArguments().getString("text")
        getView()?.findViewById<TextView>(R.id.state_tv)?.text = stateContent

        /** Retrieve the video name from the bundle. */
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

        /** Get the path to the video resource. */
        if (videoView != null) {
            videoPath = "android.resource://" + context?.packageName + "/" + resID
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(videoPath))
        }

        /** Determine the orientation of the video. */
        videoView.setOnPreparedListener { mp ->
            mp.setOnVideoSizeChangedListener { _, _, _ ->
                val width: Int = videoView.width
                val height: Int = videoView.height

                videoOrientation = if (width >= height) {
                    "landscape"
                } else {
                    "portrait"
                }
            }
        }

        /** When video is touched, the video will "expand" to fullscreen by calling
         * [VideoFullscreenActivity]. */
        videoView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val intent = Intent(activity, VideoFullscreenActivity::class.java)
                    intent.putExtra("videoPath", videoPath)
                    intent.putExtra("videoOrientation", videoOrientation)
                    startActivity(intent)
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        /** The play button overlays the video so that when the video is touched it acts like
         * a button an triggers the expansion of the video to fullscreen.. */
        getView()?.findViewById<MaterialButton>(R.id.play_btn)?.setOnClickListener {
            val motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_DOWN,
                android.R.attr.x.toFloat(),
                android.R.attr.y.toFloat(),
                0
            )

            videoView.dispatchTouchEvent(motionEvent)
        }
    }

    /**
     * The [onResume] function resumes the video by starting at the beginning.
     */
    override fun onResume() {
        super.onResume()

        videoView.seekTo(1)
    }
}