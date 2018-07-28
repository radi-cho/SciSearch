package com.rsg.SciSearch.utils

import android.content.Context
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import kotlin.math.roundToInt

class Sizes (private val context: Context) {
    // get the screen size
    val screen = Point().also {
        (context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .apply { getSize(it) }
    }

    val videoWidth: Int = if (screen.x < screen.y) (screen.x / 1.15).roundToInt() else (screen.x / 1.2).roundToInt()
    val videoHeight: Int = (videoWidth / 1.8).roundToInt()
}