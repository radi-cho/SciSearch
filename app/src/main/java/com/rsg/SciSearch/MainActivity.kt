package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
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

    fun getArticles() {
        val db = FirebaseFirestore.getInstance()
        db.collection("articles")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val intent = Intent(this, ArticleActivity::class.java)

                            val docProps = HashMap<String, String>()
                            val docItems = HashMap<String, List<HashMap<String, String>>>()

                            docProps["title"] = document["title"] as String
                            docProps["description"] = document["description"] as String
                            docProps["intro"] = document["intro"] as String
                            docItems["items"] = document["items"] as List<HashMap<String, String>>

                            intent.putExtra("docItems", docItems)
                            intent.putExtra("docProps", docProps)
                            startActivityForResult(intent, 1)
                        }
                    }
                }
    }

    companion object {
        private val TAG = "ConversionExample"
    }
}
