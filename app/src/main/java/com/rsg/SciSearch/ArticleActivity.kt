package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_article.*
import com.rsg.SciSearch.YouTube.VideoInterface
import com.rsg.SciSearch.YouTube.YouTubeContent
import kotlin.collections.HashMap


class ArticleActivity : AppCompatActivity() {

    val videos: Array<VideoInterface> = Array(2) { VideoInterface() }

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
                        val group: List<HashMap<String, String>> =
                                document.get("items")
                                        as List<HashMap<String, String>>

                        group.forEach({i ->
                            val test = TextView(this)
                            test.text = i.get("type")
                            articleContent.addView(test)
                        })
                    }
                }

//        videos.forEach { video ->
//            val option = ThumbnailLayout(this).createLayout(video)
//            option.setOnClickListener { clickCallback(video.id) }
//            articleContent.addView(option)
//        }

        articleContent.invalidate()
    }

    private fun clickCallback(video: String) {
        val intent = Intent(this, YouTubeContent::class.java)
        intent.putExtra("VIDEO", video)
        startActivityForResult(intent, 1)
    }
}
