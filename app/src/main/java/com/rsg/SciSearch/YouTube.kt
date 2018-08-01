package com.rsg.SciSearch.YouTube

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.rsg.SciSearch.R
import kotlinx.android.synthetic.main.activity_youtube.*
import java.io.IOException
import java.io.InputStream
import java.net.URL
import kotlin.math.roundToInt

class Sizes(private val context: Context) {
    // get the screen size
    val screen = Point().also {
        (context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .apply { getSize(it) }
    }

    val videoWidth: Int = if (screen.x < screen.y) (screen.x / 1.15).roundToInt() else (screen.x / 1.2).roundToInt()
    val videoHeight: Int = (videoWidth / 1.8).roundToInt()
    val thumbWidth: Int = if (screen.x < screen.y) (screen.x / 1.1).roundToInt() else (screen.x / 1.15).roundToInt()
    val thumbHeight: Int = (thumbWidth / 1.775).roundToInt()
    val thumbFontSize: Float = (thumbWidth / 10).toFloat()
}

class VideoInterface {
    val id: String = "H5cb55q4nMM"
    val title: String = "Radi Cho unboxing"
}

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
    fun createLayout(video: VideoInterface): RelativeLayout {
        val sizes = Sizes(context)

        val relativeLayout = RelativeLayout(context)
        val optionLayout: LinearLayout.LayoutParams = LinearLayout.LayoutParams(sizes.thumbWidth, sizes.thumbHeight)
        optionLayout.setMargins(1, 3, 1, 3)
        relativeLayout.layoutParams = optionLayout

        val contentSizes: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                sizes.thumbWidth,
                sizes.thumbHeight
        )

        val image = ImageView(context)
        image.layoutParams = contentSizes

        image.background = ColorDrawable(Color.rgb(220, 220, 220))
        loadRemoteThumbnail("https://img.youtube.com/vi/${video.id}/mqdefault.jpg", image)
        image.id = R.id.thumbImage
        relativeLayout.addView(image)

        thumbText.layoutParams = contentSizes
        thumbText.gravity = Gravity.CENTER
        thumbText.text = video.title
        thumbText.textSize = sizes.thumbFontSize
        relativeLayout.addView(thumbText)
        return relativeLayout
    }

    private fun finishCallback(): Boolean {
        thumbText.visibility = View.INVISIBLE
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
