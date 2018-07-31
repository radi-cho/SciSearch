package com.rsg.SciSearch.utils

import android.content.Context
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlin.math.roundToInt
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.print.PrintHelper
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.rsg.SciSearch.R
import org.w3c.dom.Text
import java.io.IOException
import java.io.InputStream
import java.net.URL


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
}

class ThumbnailLayout(private val context: Context) {
    val videoTitle = TextView(context)
    fun createLayout(videoId: String): RelativeLayout {
        val sizes = Sizes(context)

        val optionSizes: LinearLayout.LayoutParams = LinearLayout.LayoutParams(sizes.thumbWidth, sizes.thumbHeight)
        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams = optionSizes
        val image = ImageView(context)
        image.layoutParams = optionSizes

        image.background = ColorDrawable(Color.rgb(220, 220, 220))
        loadRemoteThumbnail("https://img.youtube.com/vi/$videoId/mqdefault.jpg", image)
        image.id = R.id.thumbImage
        relativeLayout.addView(image)

        videoTitle.layoutParams = optionSizes
        videoTitle.gravity = Gravity.CENTER
        videoTitle.text = "Hello from me 12345678 12345678 12345678 12345678 87654321"

        relativeLayout.addView(videoTitle)
        return relativeLayout
    }

    fun finishCallback(): Boolean {
        videoTitle.visibility = View.INVISIBLE
        return true
    }

    fun loadRemoteThumbnail(url: String, imageView: ImageView) {
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
