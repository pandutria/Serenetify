package com.example.dinus.feature.article

import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dinus.R

class DetailArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_article)

        val webView: WebView = findViewById(R.id.webView)
        val link = intent.getStringExtra("link")

        if (!link.isNullOrEmpty()) {
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(link)
        } else {
            Toast.makeText(this, "Invalid or missing link", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}