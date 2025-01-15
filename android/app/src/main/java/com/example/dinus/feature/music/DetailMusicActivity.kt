package com.example.dinus.feature.music

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.adapter.MusicTwoAdapter
import com.example.dinus.model.Music
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailMusicActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvPencipta: TextView
    private lateinit var tvTotalDuration: TextView
    private lateinit var image: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnPlay: ImageView
    private lateinit var btnPause: ImageView
    private var mediaPlayer: MediaPlayer? = null
    private var handler = Handler()

    private lateinit var musicTwoAdapter: MusicTwoAdapter
    private var musicListTwo: MutableList<Music> = mutableListOf()
    private lateinit var databaseReferenceMusicTwo: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_music)
        enableEdgeToEdge()

        tvName = findViewById(R.id.tvName)
        tvPencipta = findViewById(R.id.tvPencipta)
        tvTotalDuration = findViewById(R.id.tvTotalDuration)
        image = findViewById(R.id.image)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        progressBar = findViewById(R.id.progressBar)

        val musicUrl = intent.getStringExtra("music") ?: return
        val name = intent.getStringExtra("name") ?: return
        val pencipta = intent.getStringExtra("pencipta") ?: return
        val imageUrl = intent.getStringExtra("image") ?: return
        val totalDuration = intent.getStringExtra("duration") ?: return

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        tvName.text = name
        tvPencipta.text = "by $pencipta"
        tvTotalDuration.text = totalDuration

        Glide.with(this)
            .load(imageUrl)
            .into(image)

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(musicUrl)
                prepareAsync() // Gunakan prepareAsync() untuk menghindari blocking UI thread
                setOnPreparedListener {
                    start()
                    updateProgressBar()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("DetailMusicActivity", "Error playing MP3: what=$what, extra=$extra")
                    true
                }
            }
        } catch (e: Exception) {
            Log.e("DetailMusicActivity", "Error playing MP3: ${e.message}")
            e.printStackTrace()
        }

        btnPause.setOnClickListener {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    btnPlay.visibility = View.VISIBLE
                    btnPause.visibility = View.GONE
                }
            }
        }


        btnPlay.setOnClickListener {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    updateProgressBar()
                    btnPlay.visibility = View.GONE
                    btnPause.visibility = View.VISIBLE
                }
            }
        }

        val rvMusicTwo: RecyclerView = findViewById(R.id.rvMusic2)

        rvMusicTwo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        musicTwoAdapter = MusicTwoAdapter(musicListTwo)
        rvMusicTwo.adapter = musicTwoAdapter

        databaseReferenceMusicTwo =
            FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("musics")

        fetchDataMusic()

    }

    private fun fetchDataMusic() {
        databaseReferenceMusicTwo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                musicListTwo.clear()
                var count = 0
                for (musicSnapshot in dataSnapshot.children) {
                    if (count >= 3) break
                    val music = musicSnapshot.getValue(Music::class.java)
                    music?.let {
                        musicListTwo.add(it)
                    }
                    count++
                }
                musicTwoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun updateProgressBar() {
        mediaPlayer?.let { player ->
            val duration = player.duration
            progressBar.max = duration

            handler.postDelayed(object : Runnable {
                override fun run() {
                    val currentPosition = player.currentPosition
                    progressBar.progress = currentPosition
                    if (player.isPlaying) {
                        handler.postDelayed(this, 100)
                    }
                }
            }, 100)
        }
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }
}