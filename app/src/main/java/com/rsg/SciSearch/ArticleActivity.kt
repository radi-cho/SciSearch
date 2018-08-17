package com.rsg.SciSearch

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_article.*
import kotlin.collections.HashMap
import com.rsg.SciSearch.ImageLoader.*
import com.rsg.SciSearch.Utils.*
import com.rsg.SciSearch.YouTube.YouTubeContent

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val sizes = Sizes(this)
        val UI = UIElements(this)

        articleContent.layoutParams.width = sizes.layoutWidth
        updateActionBar("Loading...")

        val documentProps: HashMap<String, String>
        val documentItems: HashMap<String, List<HashMap<String, String>>>
        if (intent.hasExtra("docProps") && intent.hasExtra("docItems")) {
            documentProps = intent.extras.getSerializable("docProps") as HashMap<String, String>
            documentItems = intent.extras.getSerializable("docItems") as HashMap<String, List<HashMap<String, String>>>
        } else {
            return
        }

        updateActionBar(documentProps["title"] as String)
        UI.renderDescription(documentProps["description"] as String, articleContent)
        UI.renderArticleText(documentProps["intro"] as String, articleContent, sizes)

        val items = documentItems["items"] as ArrayList<HashMap<String, String>>
        items.forEach({ i ->
            val type = i["type"]
            if (type != null) {
                when (type) {
                    "youtube" -> {
                        val thumb = RemoteImageLoader(this).createLayout(
                                "https://img.youtube.com/vi/${i["videoId"]}/mqdefault.jpg",
                                i["title"] as String
                        )

                        val playerIcon: ImageView = LocalImageLoader(this).getImageView(
                                ResourcesCompat.getDrawable(resources, R.drawable.ic_play_arrow, null) as Drawable
                        )

                        val params = RelativeLayout.LayoutParams(sizes.playIconSize, sizes.playIconSize)
                        params.addRule(RelativeLayout.CENTER_IN_PARENT)
                        playerIcon.layoutParams = params

                        thumb.setOnClickListener { clickCallback(i["videoId"].toString()) }
                        thumb.addView(playerIcon)
                        articleContent.addView(thumb)
                    }

                    "text" -> {
                        val text = i["text"] as String
                        UI.renderArticleText(text, articleContent, sizes)
                    }

                    "topic" -> {
                        UI.renderTopicName(i["title"] as String, articleContent)
                    }

                    "image" -> {
                        val img = RemoteImageLoader(this).createLayout(
                                i["url"] as String,
                                i["alt"] as String
                        )
                        articleContent.addView(img)
                    }
                }
            }
        })

        articleContent.invalidate()
    }

    private fun clickCallback(video: String) {
        val intent = Intent(this, YouTubeContent::class.java)
        intent.putExtra("VIDEO", video)
        startActivityForResult(intent, 1)
    }

    fun updateActionBar(title: String) {
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.title = title
        actionBar.show()
    }
}
