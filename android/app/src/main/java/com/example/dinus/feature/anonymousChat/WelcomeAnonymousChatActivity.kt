package com.example.dinus.feature.anonymousChat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dinus.R
import com.example.dinus.feature.quotes.QuoteActivity

class WelcomeAnonymousChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_anonymous_chat)

        findViewById<AppCompatButton>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, AnonymousChatActivity::class.java))
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}