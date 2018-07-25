package com.rsg.SciSearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.youtube_source.view.*

class ArticleActivity : YouTubeBaseActivity() {

    companion object {
        val VIDEO_ID: String = "9tbxDgcv74c"
        val YOUTUBE_API_KEY: String = "AIzaSyCOPi8Vq3IdlHPjwIKh5ZZsW22fCwkew0Q"
    }

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
    }
}
