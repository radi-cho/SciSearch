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

        val document: HashMap<String, Any>
        if (intent.hasExtra("document")) {
            document = intent.extras.getSerializable("document") as HashMap<String, Any>
        } else {
            return
        }

        updateActionBar(document["title"] as String)
        UI.renderDescription(document["description"] as String, articleContent)

        val items = document["items"] as ArrayList<HashMap<String, String>>
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

                        thumb.setOnClickListener {
                            clickCallback(i as HashMap<String, Any>)
                        }
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

    private fun clickCallback(video: HashMap<String, Any>) {
        val intent = Intent(this, YouTubeContent::class.java)
        intent.putExtra("title", video["title"] as String)
        intent.putExtra("text", video["text"] as String)
        intent.putExtra("id", video["videoId"] as String)

        val quotes = HashMap<String, List<HashMap<String, String>>>()
        quotes["items"] = video["quotes"] as List<HashMap<String, String>>
        intent.putExtra("quotes", quotes)

        startActivityForResult(intent, 1)
    }

    private fun updateActionBar(title: String) {
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)
        actionBar.title = title
        actionBar.show()
    }
}
