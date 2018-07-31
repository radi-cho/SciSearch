package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import com.rsg.SciSearch.utils.ThumbnailLayout
import android.widget.TextView
import com.rsg.SciSearch.utils.Sizes


class ArticleActivity : AppCompatActivity() {

    val videos: Array<String> = arrayOf("H5cb55q4nMM", "rcOFV4y5z8c")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        val sizes = Sizes(this)


        val loadingImageText = TextView(this)
        loadingImageText.text = "Hello, Please wait until loading"
        articleContent.addView(loadingImageText)

        videos.forEach { videoId ->
            //            val option = ImageView(this)
//            option.setOnClickListener { clickCallback(videoId) }
//
            val option = ThumbnailLayout(this).createLayout(videoId)

            articleContent.addView(option)
        }

        articleContent.invalidate()
    }

    fun clickCallback(video: String) {
        val intent = Intent(this, YouTube::class.java)
        intent.putExtra("VIDEO", video)
        startActivityForResult(intent, 1)
    }
}
