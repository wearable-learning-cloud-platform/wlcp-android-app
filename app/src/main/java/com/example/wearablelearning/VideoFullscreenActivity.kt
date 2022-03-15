package com.example.wearablelearning

import android.R.attr.x
import android.R.attr.y
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess


class VideoFullscreenActivity : AppCompatActivity() {
    companion object {
        var isPaused = 1.0 //1.0=true, 0.0=false, 0.5=almost
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_video_fullscreen)

        var videoPath = intent.getSerializableExtra("videoPath") as String
        var videoOrientation = intent.getSerializableExtra("videoOrientation") as String

        val videoView = findViewById<VideoView>(R.id.activity_videoView)
        val mediaController = MediaController(this@VideoFullscreenActivity)
        mediaController.setAnchorView(videoView)
        mediaController.visibility = View.GONE

        if (videoView != null && videoPath != null) {
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(videoPath))
            videoView.seekTo(1)

            if(videoOrientation == "landscape") {
                videoView.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                videoView.layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            }
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
                    //if video is paused, then play
                    if (isPaused == 1.0) {
                        videoView.start()
                        isPaused = 0.0
                        clearDim(videoView)
                        playBtn.icon = this@VideoFullscreenActivity.let { ContextCompat.getDrawable(it, R.drawable.ic_pause_foreground)}
                        playBtn.iconTint = ContextCompat.getColorStateList(this@VideoFullscreenActivity, R.color.transparent)
                    }
                    //if video is playing and screen tapped, then signal that player can pause by dimming screen and showing pause button for 1.5s
                    else if(isPaused == 0.0) {
                        isPaused = 0.5
                        applyDim(videoView, 0.5f)
                        playBtn.icon = this@VideoFullscreenActivity.let { ContextCompat.getDrawable(it, R.drawable.ic_pause_foreground)}
                        playBtn.iconTint = ContextCompat.getColorStateList(this@VideoFullscreenActivity, R.color.white)

                        Timer("SettingUp", false).schedule(1500) {
                            if(isPaused == 0.5) {
                                isPaused = 0.0
                                clearDim(videoView)
                                playBtn.iconTint = ContextCompat.getColorStateList(
                                    this@VideoFullscreenActivity,
                                    R.color.transparent
                                )
                            }
                        }
                    }
                    //if video is playing and pause signal already triggered, then pause
                    else if (isPaused == 0.5) {
                        videoView.pause()
                        isPaused = 1.0
                        playBtn.icon = this@VideoFullscreenActivity.let { ContextCompat.getDrawable(it, R.drawable.ic_play_foreground)}
                        playBtn.iconTint = ContextCompat.getColorStateList(this@VideoFullscreenActivity, R.color.white)
                    }
                }
            }

            v?.onTouchEvent(event) ?: true
        })

        videoView.setOnCompletionListener {
            videoView.seekTo(1)
            isPaused = 0.5

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

            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun applyDim(parent: VideoView, dimAmount: Float) {
        val dim: Drawable = ColorDrawable(Color.BLACK)
        dim.setBounds(0, 0, parent.width, parent.height)
        dim.alpha = (255 * dimAmount).toInt()
        val overlay = parent.overlay
        overlay.add(dim)
    }

    private fun clearDim(parent: VideoView) {
        val overlay = parent.overlay
        overlay.clear()
    }
}