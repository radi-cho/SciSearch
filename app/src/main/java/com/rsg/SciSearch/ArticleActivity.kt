package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_article.*
import kotlin.collections.HashMap
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.rsg.SciSearch.YouTube.YouTubeContent
import com.rsg.SciSearch.YouTube.ThumbnailLayout
import com.rsg.SciSearch.Utils.*

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val sizes = Sizes(this)
        val UI = UIElements(this)

        articleContent.layoutParams.width = sizes.layoutWidth
        updateActionBar("Loading...")

        val incomingDocumentId: String
        if (intent.hasExtra("docId")) {
            incomingDocumentId = intent.extras.getString("docId")
        } else {
            return
        }

        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        db.firestoreSettings = settings
        db.collection("articles")
                .document(incomingDocumentId).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result

                        updateActionBar(document["title"] as String)
                        UI.renderDescription(document["description"] as String, articleContent)
                        UI.renderHorizotalLine(2.dpToPx(), articleContent)
                        UI.renderArticleText(document["intro"] as String, articleContent, sizes)

                        val items = document.get("items") as List<HashMap<String, String>>
                        items.forEach({ i ->
                            val type = i["type"]
                            if (type != null) {
                                when (type) {
                                    "youtube" -> {
                                        val thumb = ThumbnailLayout(this).createLayout(i)
                                        thumb.setOnClickListener { clickCallback(i["videoId"].toString()) }
                                        articleContent.addView(thumb)
                                    }

                                    "text" -> {
                                        val text = i["text"] as String
                                        UI.renderArticleText(text, articleContent, sizes)
                                    }

                                    "topic" -> {
                                        UI.renderTopicName(i["title"] as String, articleContent)
                                    }
                                }
                            }
                        })
                    }
                }

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
