package com.rsg.SciSearch.YouTube

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube.*
import java.io.IOException
import java.io.InputStream
import java.net.URL
import com.rsg.SciSearch.R
import com.rsg.SciSearch.Utils.Sizes

class YouTubeContent : YouTubeBaseActivity() {
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
        player.layoutParams = LinearLayout.LayoutParams(sizes.videoWidth, sizes.videoHeight)
        player.initialize(YOUTUBE_API_KEY, getYoutubePlayerInit(incomingVideoId))
        youtubeSource.addView(player)

        youtubeSource.invalidate()
    }

    private fun addMissingMessage() {
        val missingMessage = TextView(this)
        missingMessage.text = "The video cannot be loaded right now."
        missingMessage.textSize = 20.toFloat()
        missingMessage.gravity = Gravity.CENTER_HORIZONTAL
        youtubeSource.addView(missingMessage)
    }
}

class ThumbnailLayout(private val context: Context) {
    private val thumbText = TextView(context)
    fun createLayout(video: HashMap<String, String>): RelativeLayout {
        val sizes = Sizes(context)

        val relativeLayout = RelativeLayout(context)
        val optionLayout: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, sizes.thumbHeight)
        optionLayout.setMargins(1, 3, 1, 3)
        relativeLayout.layoutParams = optionLayout

        val contentSizes: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                sizes.thumbHeight
        )

        val image = ImageView(context)
        image.layoutParams = contentSizes

        image.background = ColorDrawable(Color.rgb(220, 220, 220))
        loadRemoteThumbnail("https://img.youtube.com/vi/${video.get("videoId")}/mqdefault.jpg", image)
        image.id = R.id.thumbImage
        relativeLayout.addView(image)

        thumbText.layoutParams = contentSizes
        thumbText.gravity = Gravity.CENTER
        thumbText.text = video.get("title")
        thumbText.textSize = sizes.thumbFontSize
        relativeLayout.addView(thumbText)
        return relativeLayout
    }

    private fun finishCallback(): Boolean {
        thumbText.post({ thumbText.visibility = View.INVISIBLE })
        return true
    }

    private fun loadRemoteThumbnail(url: String, imageView: ImageView) {
        Thread(Runnable {
            try {
                val drawable = Drawable.createFromStream(URL(url).content as InputStream, "src")
                imageView.post({ imageView.setImageDrawable(drawable) })
                finishCallback()
            } catch (e: IOException) {
                // TODO: Add Firebase/Fabric Crashlytics solution here
            }
        }).start()
    }

}
