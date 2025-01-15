package com.example.dinus.feature.video

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dinus.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class DetailVideoActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_video)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tvTitle).text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.tvDesc).text = intent.getStringExtra("desc")
        findViewById<TextView>(R.id.tvSource).text = intent.getStringExtra("source")

        val link = intent.getStringExtra("link") ?: return
        initializePlayer(link)
    }

    private fun initializePlayer(videoUrl: String) {
        val playerView: PlayerView = findViewById(R.id.exo)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player?.setMediaItem(mediaItem)

        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}