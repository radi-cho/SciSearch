package com.rsg.SciSearch

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
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
    }

    fun getArticles() {
        val db = FirebaseFirestore.getInstance()
        db.collection("articles")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val intent = Intent(this, ArticleActivity::class.java)
                            intent.putExtra("doc", document.id)
                            startActivityForResult(intent, 1)
                        }
                    }
                }
    }

    companion object {
        private val TAG = "ConversionExample"
    }
}
