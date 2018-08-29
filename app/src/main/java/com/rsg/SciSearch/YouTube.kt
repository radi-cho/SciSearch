package com.rsg.SciSearch.YouTube

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube.*
import com.rsg.SciSearch.R
import com.rsg.SciSearch.Utils.Sizes
import com.rsg.SciSearch.Utils.dpToPx

class YouTubeContent: YouTubeBaseActivity() {
    lateinit var player: YouTubePlayer
    private lateinit var incomingVideoId: String

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
        if (intent.hasExtra("id")) {
            incomingVideoId = intent.extras.getString("id")
        } else {
            addMissingMessage()
            return
        }

        val playerView = YouTubePlayerView(this)
        val playerParams = LinearLayout.LayoutParams(sizes.videoWidth, sizes.videoHeight)
        playerParams.setMargins(0, 8.dpToPx(), 0, 5.dpToPx())
        playerView.layoutParams = playerParams

        playerView.initialize(YOUTUBE_API_KEY, getYoutubePlayerInit(incomingVideoId))
        playerLayout.addView(playerView)

        playerLayout.invalidate()

        if (intent.hasExtra("quotes")) {
            val quotesSerializable = intent.getSerializableExtra("quotes") as HashMap<String, List<HashMap<String, Any>>>
            val quotesList = quotesSerializable["items"] as List<HashMap<String, Any>>

            // Render the quotes
            quotesList.forEach {
                val quoteButton = Button(this)
                quoteButton.text = it["text"] as String
                setQuoteClickListener(quoteButton, it["time"] as Long)
                youtubeActivityLayout.addView(quoteButton)
            }
        } else {
            addMissingMessage()
            return
        }
    }

    private fun addMissingMessage() {
        val missingMessage = TextView(this)
        missingMessage.text = "The video cannot be loaded right now."
        missingMessage.textSize = 20.toFloat()
        missingMessage.gravity = Gravity.CENTER_HORIZONTAL
        playerLayout.addView(missingMessage)
    }

    private fun setQuoteClickListener (quote: View, seekTime: Long) {
        val ms = seekTime.toInt()
        quote.setOnClickListener {
            if (player.isPlaying) {
                player.seekToMillis(ms)
            } else {
                player.cueVideo(incomingVideoId, ms)
                player.play()
            }
        }
    }
}
