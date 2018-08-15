package com.rsg.SciSearch.Utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import kotlin.math.roundToInt

// helpers and methods
fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

class Sizes(private val context: Context) {
    fun getArticleTextParams(): LayoutParams {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 5.dpToPx(), 0, 5.dpToPx())
        return params
    }

    // get the screen size
    val screen = Point().also {
        (context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .apply { getSize(it) }
    }

    // layout sizes
    val layoutWidth: Int = if (screen.x < screen.y) (screen.x / 1.1).roundToInt() else (screen.x / 1.15).roundToInt()

    // text sizes
    val articleTextSize: Float = 18f

    // media sizes
    val videoWidth: Int = if (screen.x < screen.y) (screen.x / 1.05).roundToInt() else (screen.x / 1.1).roundToInt()
    val videoHeight: Int = (videoWidth / 1.8).roundToInt()
    val thumbHeight: Int = (layoutWidth / 1.775).roundToInt()
    val thumbFontSize: Float = 35f

    val playIconSize: Int = layoutWidth / 5
}

class UIElements(private val context: Context) {
    fun renderHorizotalLine(layout: LinearLayout) {
        val hr = View(context)

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.dpToPx())
        params.setMargins(0, 3.dpToPx(), 0, 2.dpToPx())
        hr.layoutParams = params

        hr.background = ColorDrawable(Color.GRAY)
        layout.addView(hr)
    }

    fun renderDescription (text: String, layout: LinearLayout) {
        val displayText = TextView(context)
        displayText.text = text
        displayText.setTextColor(Color.BLACK)
        displayText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        displayText.gravity = Gravity.CENTER_HORIZONTAL

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0, 3.dpToPx())
        displayText.layoutParams = params

        layout.addView(displayText)
        renderHorizotalLine(layout)
    }

    fun renderArticleText (text: String, layout: LinearLayout, sizes: Sizes) {
        val displayText = TextView(context)
        displayText.text = text
        displayText.layoutParams = sizes.getArticleTextParams()
        displayText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes.articleTextSize)
        layout.addView(displayText)
    }

    fun renderTopicName (text: String, layout: LinearLayout) {
        val textView = TextView(context)
        val textContent = SpannableString(text)
        textContent.setSpan(StyleSpan(Typeface.ITALIC), 0, textContent.length, 0)

        textView.text = textContent
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.LEFT
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
        layout.addView(textView)

        // add horizontal line below the sub-title
        renderHorizotalLine(layout)
    }
}
