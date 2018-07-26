package com.rsg.SciSearch

import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.youtube_source.view.*
import kotlin.math.roundToInt

class ArticleActivity : YouTubeBaseActivity() {

    // YouTube Test config
    companion object {
        val VIDEO_ID: String = "9tbxDgcv74c"
        val YOUTUBE_API_KEY: String = "AIzaSyCOPi8Vq3IdlHPjwIKh5ZZsW22fCwkew0Q"
    }

    // Initialize YouTube player
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    var player: YouTubePlayer? = null
    private fun initUI() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?, p2: Boolean) {
                player = youTubePlayer
                player?.cueVideo(VIDEO_ID)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "Something went wrong while loading the video", Toast.LENGTH_SHORT).show()
            }

        }

        include1.TestYTPlayer.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        initUI()
        // get the screen size
        val screenWidth: Int = Point().also {
            (this.getSystemService(WINDOW_SERVICE) as WindowManager)
                    .defaultDisplay
                    .apply { getSize(it) }
        }.x

        val videoWidth: Int = (screenWidth / 1.1).roundToInt()
        val videoHeight: Int = videoWidth / 2
        include1.TestYTPlayer.layoutParams = LinearLayout.LayoutParams(videoWidth, videoHeight)
    }
}
