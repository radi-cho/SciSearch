package com.rsg.SciSearch.YouTube

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube.*
import com.rsg.SciSearch.R
import com.rsg.SciSearch.Utils.Sizes
import com.rsg.SciSearch.Utils.dpToPx
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener



class YouTubeContent: YouTubeBaseActivity() {
    lateinit var player: YouTubePlayer

    companion object {
        const val YOUTUBE_API_KEY: String = "AIzaSyCOPi8Vq3IdlHPjwIKh5ZZsW22fCwkew0Q"
    }

    private fun getYoutubePlayerInit(videoId: String): YouTubePlayer.OnInitializedListener {
        return object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean) {
                player = youTubePlayer
                player.cueVideo(videoId)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "Something went wrong while loading the video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val sizes = Sizes(this)
        val incomingVideoId: String
        if (intent.hasExtra("VIDEO")) {
            incomingVideoId = intent.extras.getString("VIDEO")
        } else {
            addMissingMessage()
            return
        }

        val playerView = YouTubePlayerView(this)
        val playerParams = LinearLayout.LayoutParams(sizes.videoWidth, sizes.videoHeight)
        playerParams.setMargins(0, 8.dpToPx(),
                0, 0)
        playerView.layoutParams = playerParams

        playerView.initialize(YOUTUBE_API_KEY, getYoutubePlayerInit(incomingVideoId))
        playerLayout.addView(playerView)

        playerLayout.invalidate()

        seekVid.setOnClickListener {
            val ms = 20000
            // TODO: Use this method to seek the video to a quote
            if (player.isPlaying) {
               player.seekToMillis(ms)
            } else {
                player.cueVideo(incomingVideoId, ms)
                player.play()
            }
        }
    }

    private fun addMissingMessage() {
        val missingMessage = TextView(this)
        missingMessage.text = "The video cannot be loaded right now."
        missingMessage.textSize = 20.toFloat()
        missingMessage.gravity = Gravity.CENTER_HORIZONTAL
        playerLayout.addView(missingMessage)
    }
}
