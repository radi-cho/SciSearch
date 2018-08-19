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

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        message.setOnClickListener({ _ ->
            val intent = Intent(this, ArticleActivity::class.java)
            startActivityForResult(intent, 1)
        })

        getArticles()
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
    }

    private fun getArticles() {
        val db = FirebaseFirestore.getInstance()
        db.collection("articles")
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(25)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val text = TextView(this)
                            text.text = document["title"] as String
                            text.setOnClickListener {
                                val intent = Intent(this, ArticleActivity::class.java)
                                val doc = HashMap<String, Any>()
                                doc["title"] = document["title"] as String
                                doc["description"] = document["description"] as String
                                doc["items"] = document["items"] as ArrayList<HashMap<String, String>>

                                intent.putExtra("document", doc)
                                startActivityForResult(intent, 1)
                            }
                            content.addView(text)
                        }
                    }
                }
    }

    companion object {
        private val TAG = "ConversionExample"
    }
}
