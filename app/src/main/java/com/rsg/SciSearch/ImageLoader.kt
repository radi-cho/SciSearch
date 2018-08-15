package com.rsg.SciSearch.ImageLoader

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.rsg.SciSearch.R
import com.rsg.SciSearch.Utils.Sizes
import java.io.IOException
import java.io.InputStream
import java.net.URL


class RemoteImageLoader(private val context: Context) {
    private val thumbText = TextView(context)
    fun createLayout(url: String, alt: String): RelativeLayout {
        val sizes = Sizes(context)

        val relativeLayout = RelativeLayout(context)
        val optionLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, sizes.thumbHeight)
        optionLayoutParams.setMargins(1, 3, 1, 3)
        relativeLayout.layoutParams = optionLayoutParams

        val contentSizes = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                sizes.thumbHeight
        )

        val image = ImageView(context)
        image.layoutParams = contentSizes

        image.background = ColorDrawable(Color.rgb(220, 220, 220))
        loadRemoteThumbnail(url, image, relativeLayout, sizes)
        image.id = R.id.thumbImage
        relativeLayout.addView(image)

        thumbText.layoutParams = contentSizes
        thumbText.gravity = Gravity.CENTER
        thumbText.text = alt
        thumbText.textSize = sizes.thumbFontSize
        relativeLayout.addView(thumbText)
        return relativeLayout
    }

    private fun finishCallback(): Boolean {
        thumbText.post({ thumbText.visibility = View.INVISIBLE })
        return true
    }

    private fun loadRemoteThumbnail(url: String, imageView: ImageView, layout: RelativeLayout, sizes: Sizes) {
        Thread(Runnable {
            try {
                val drawable = Drawable.createFromStream(URL(url).content as InputStream, "src")

                // Adapt the layout size to the image
                val ratio: Float = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
                val imageHeight: Int = (sizes.layoutWidth / ratio).toInt()
                val relativeParams = RelativeLayout.LayoutParams(sizes.layoutWidth, imageHeight)

                imageView.post({
                    imageView.setImageDrawable(drawable)
                    imageView.layoutParams = relativeParams
                })

                val linearParams = LinearLayout.LayoutParams(sizes.layoutWidth, imageHeight)

                layout.post({
                    layout.layoutParams = linearParams
                })

                // Loaded successfully
                finishCallback()
            } catch (e: IOException) {
                // TODO: Add Firebase/Fabric Crashlytics solution here
            }
        }).start()
    }

}

class LocalImageLoader(private val context: Context) {
    fun getImageView(drawable: Drawable): ImageView {
//        val sizes = Sizes(context)
        val image = ImageView(context)
        image.setImageDrawable(drawable)

        return image
    }
}
