package com.example.wearablelearning

import android.R.attr.x
import android.R.attr.y
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton


class VideoFullscreenActivity : AppCompatActivity() {
    companion object {
        var isPaused = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_video_fullscreen)

        var videoPath = intent.getSerializableExtra("videoPath") as String

        val videoView = findViewById<VideoView>(R.id.activity_videoView)
        val mediaController = MediaController(this@VideoFullscreenActivity)
        mediaController.setAnchorView(videoView)
        mediaController.visibility = View.GONE

        if (videoView != null && videoPath != null) {
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(videoPath))
            videoView.seekTo(1)
        }

        val playBtn = findViewById<MaterialButton>(R.id.play_btn)

        playBtn.setOnClickListener{
            val motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_DOWN,
                x.toFloat(),
                y.toFloat(),
                0
            )

            videoView.dispatchTouchEvent(motionEvent)
        }

        videoView?.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isPaused) {
                        videoView.start()
                        isPaused = false
                        playBtn.icon = this@VideoFullscreenActivity.let { ContextCompat.getDrawable(it, R.drawable.ic_pause_foreground)}
                        playBtn.iconTint = ContextCompat.getColorStateList(this@VideoFullscreenActivity, R.color.transparent)
                    } else {
                        videoView.pause()
                        isPaused = true
                        playBtn.icon = this@VideoFullscreenActivity.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
                        playBtn.iconTint = ContextCompat.getColorStateList(this@VideoFullscreenActivity, R.color.white)
                    }
                }
            }

            v?.onTouchEvent(event) ?: true
        })

        videoView.setOnCompletionListener {
            videoView.seekTo(1)
            isPaused = false

            val motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_DOWN,
                x.toFloat(),
                y.toFloat(),
                0
            )

            videoView.dispatchTouchEvent(motionEvent)
        }

        val closeBtn: Button = findViewById(R.id.close_btn)

        closeBtn.setOnClickListener{
            if(videoView != null && videoView.isPlaying) {
                videoView.stopPlayback()
            }

            finish()
            overridePendingTransition(0, 0)
        }
    }
}