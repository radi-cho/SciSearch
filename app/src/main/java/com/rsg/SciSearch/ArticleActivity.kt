package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_article.*
import com.rsg.SciSearch.YouTube.ThumbnailLayout
import com.rsg.SciSearch.YouTube.VideoInterface
import com.rsg.SciSearch.YouTube.YouTube


class ArticleActivity : AppCompatActivity() {

    val videos: Array<VideoInterface> = Array(2) {VideoInterface()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        videos.forEach { video ->
            val option = ThumbnailLayout(this).createLayout(video)
            option.setOnClickListener { clickCallback(video.id) }
            articleContent.addView(option)
        }

        articleContent.invalidate()
    }

    private fun clickCallback(video: String) {
        val intent = Intent(this, YouTube::class.java)
        intent.putExtra("VIDEO", video)
        startActivityForResult(intent, 1)
    }
}
