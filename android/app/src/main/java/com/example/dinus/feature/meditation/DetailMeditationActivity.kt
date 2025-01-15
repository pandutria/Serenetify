package com.example.dinus.feature.meditation

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dinus.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.widget.TextView

class DetailMeditationActivity : AppCompatActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvSource: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_meditation)


        playerView = findViewById(R.id.exo)
        tvTitle = findViewById(R.id.tvTitle)
        tvDesc = findViewById(R.id.tvDesc)
        tvSource = findViewById(R.id.tvSource)

        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")
        val videoUrl = intent.getStringExtra("video")
        val source = intent.getStringExtra("source")


        tvTitle.text = title
        tvDesc.text = desc
        tvSource.text = "Source: Youtube " + source

        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        videoUrl?.let {
            val mediaItem = MediaItem.fromUri(Uri.parse(it))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        exoPlayer.release()
    }
}