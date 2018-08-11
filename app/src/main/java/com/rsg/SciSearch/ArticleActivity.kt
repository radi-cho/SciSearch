package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.rsg.SciSearch.YouTube.ThumbnailLayout
import kotlinx.android.synthetic.main.activity_article.*
import com.rsg.SciSearch.YouTube.YouTubeContent
import kotlin.collections.HashMap


class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val incomingDocumentId: String
        if (intent.hasExtra("doc")) {
            incomingDocumentId = intent.extras.getString("doc")
        } else {
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("articles")
                .document(incomingDocumentId).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        val items: List<HashMap<String, String>> =
                                document.get("items")
                                        as List<HashMap<String, String>>

                        items.forEach({ i ->
                            val type = i.get("type")
                            if (type != null) {
                                when (i.get("type")) {
                                    "youtube" -> {
                                        val thumb = ThumbnailLayout(this).createLayout(i)
                                        thumb.setOnClickListener { clickCallback(i.get("videoId").toString()) }
                                        articleContent.addView(thumb)
                                    }

                                    "text" -> {
                                        val displayText = TextView(this)
                                        displayText.text = i.get("text")
                                        articleContent.addView(displayText)
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
}
