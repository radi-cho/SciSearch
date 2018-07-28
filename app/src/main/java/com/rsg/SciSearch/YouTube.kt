package com.rsg.SciSearch

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.rsg.SciSearch.utils.Sizes
import kotlinx.android.synthetic.main.activity_youtube.*

class YouTube : YouTubeBaseActivity() {

    // YouTube Test config
    companion object {
        val YOUTUBE_API_KEY: String = "AIzaSyCOPi8Vq3IdlHPjwIKh5ZZsW22fCwkew0Q"
    }

    fun getYoutubePlayerInit(videoId: String): YouTubePlayer.OnInitializedListener {
        val youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean) {
                youTubePlayer.cueVideo(videoId)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "Something went wrong while loading the video", Toast.LENGTH_SHORT).show()
            }
        }

        return youtubePlayerInit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val sizes = Sizes(this)
        val incomingVideoId: String
        if (getIntent().hasExtra("VIDEO")) {
            incomingVideoId = intent.extras.getString("VIDEO")
        } else {
            addMissingMessage()
            return
        }

        val player = YouTubePlayerView(this)
        player.layoutParams = LinearLayout.LayoutParams(sizes.videoWidth, sizes.videoHeight)
        player.initialize(YOUTUBE_API_KEY, getYoutubePlayerInit(incomingVideoId))
        youtubeSource.addView(player)

        youtubeSource.invalidate()
    }

    fun addMissingMessage() {
        val missingMessage = TextView(this)
        missingMessage.text = "The video cannot be loaded right now."
        missingMessage.textSize = 20.toFloat()
        missingMessage.gravity = Gravity.CENTER_HORIZONTAL
        youtubeSource.addView(missingMessage)
    }
}
