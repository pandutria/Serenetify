package com.example.dinus.feature.music

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.MusicAdapter
import com.example.dinus.adapter.MusicTwoAdapter
import com.example.dinus.model.Music
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MusicActivity : AppCompatActivity() {

    private lateinit var musicOneAdapter: MusicAdapter
    private lateinit var musicTwoAdapter: MusicTwoAdapter
    private var musicListOne: MutableList<Music> = mutableListOf()
    private var musicListTwo: MutableList<Music> = mutableListOf()

    private lateinit var databaseReferenceMusicOne: DatabaseReference
    private lateinit var databaseReferenceMusicTwo: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        enableEdgeToEdge()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val rvMusicOne: RecyclerView = findViewById(R.id.rvMusic1)
        val rvMusicTwo: RecyclerView = findViewById(R.id.rvMusic2)

        rvMusicOne.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMusicTwo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        musicOneAdapter = MusicAdapter(musicListOne)
        musicTwoAdapter = MusicTwoAdapter(musicListTwo)

        rvMusicOne.adapter = musicOneAdapter
        rvMusicTwo.adapter = musicTwoAdapter

        databaseReferenceMusicOne = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("music")  // This reference is for music list one

        databaseReferenceMusicTwo = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("musics")

        fetchDataMusic()
    }

    private fun fetchDataMusic() {
        databaseReferenceMusicOne.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                musicListOne.clear()
                for (musicSnapshot in dataSnapshot.children) {
                    val music = musicSnapshot.getValue(Music::class.java)
                    music?.let {
                        musicListOne.add(it)
                    }
                }
                Log.d("MusicActivity", "Music List One Size: ${musicListOne.size}")
                musicOneAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MusicActivity", "Database Error: ${error.message}")
            }
        })

        databaseReferenceMusicTwo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                musicListTwo.clear()
                for (musicSnapshot in dataSnapshot.children) {
                    val music = musicSnapshot.getValue(Music::class.java)
                    music?.let {
                        musicListTwo.add(it)
                    }
                }
                musicTwoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}