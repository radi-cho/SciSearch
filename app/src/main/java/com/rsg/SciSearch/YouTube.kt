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
import io.fabric.sdk.android.services.settings.IconRequest.build



class YouTubeContent: YouTubeBaseActivity() {
    companion object {
        const val YOUTUBE_API_KEY: String = "AIzaSyCOPi8Vq3IdlHPjwIKh5ZZsW22fCwkew0Q"
    }

    private fun getYoutubePlayerInit(videoId: String): YouTubePlayer.OnInitializedListener {
        return object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean) {
                youTubePlayer.cueVideo(videoId)
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

        val player = YouTubePlayerView(this)
        val playerParams = LinearLayout.LayoutParams(sizes.videoWidth, sizes.videoHeight)
        playerParams.setMargins(0, 8.dpToPx(), 0, 0)
        player.layoutParams = playerParams

        player.initialize(YOUTUBE_API_KEY, getYoutubePlayerInit(incomingVideoId))
        playerLayout.addView(player)

        playerLayout.invalidate()
    }

    private fun addMissingMessage() {
        val missingMessage = TextView(this)
        missingMessage.text = "The video cannot be loaded right now."
        missingMessage.textSize = 20.toFloat()
        missingMessage.gravity = Gravity.CENTER_HORIZONTAL
        playerLayout.addView(missingMessage)
    }
}
