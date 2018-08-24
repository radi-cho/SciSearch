package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var category: String = "recent"
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        content.removeAllViews()
        renderLoading()

        when (item.itemId) {
            R.id.navigation_home -> {
                if (category != "recent") {
                    category = "recent"
                    getArticles()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (category != "keywords") {
                    category = "keywords"
                    getArticles()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (category != "something") {
                    category = "something"
                    getArticles()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        renderLoading()
        getArticles()
    }

    private fun getQuery(): Query {
        val db = FirebaseFirestore.getInstance()
        return when (category) {
            "recent" ->
                db.collection("articles")
                        .orderBy("publishedAt", Query.Direction.DESCENDING)
                        .limit(25)

            "keywords" ->
                db.collection("articles")
                        .whereArrayContains("keywords", "computing")

            else ->
                db.collection("articles")
                        .orderBy("publishedAt", Query.Direction.ASCENDING)
                        .limit(25)
        }
    }

    private fun renderLoading() {
        val text = TextView(this)
        text.text = "Loading..."
        content.addView(text)
    }

    private fun getArticles() {
        getQuery().get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                content.removeAllViews()

                for (document in task.result) {
                    if (
                            document["title"] != null && document["title"] is String &&
                            document["description"] != null && document["description"] is String &&
                            document["items"] != null && document["items"] is ArrayList<*>
                    ) {
                        val text = TextView(this)
                        text.text = document["title"] as String
                        text.setOnClickListener {
                            val intent = Intent(this, ArticleActivity::class.java)
                            val doc = HashMap<String, Any>()

                            doc["title"] = document["title"] as String
                            doc["description"] = document["description"] as String
                            doc["items"] = document["items"] as ArrayList<HashMap<String, String>>

                            intent.putExtra("document", doc)
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivityForResult(intent, 1)

                        }

                        content.addView(text)
                    } else {
                        // TODO: Report invalid article (including id) via Crashlytics non-fatal issues
                    }
                }
            }
        }
    }
}
