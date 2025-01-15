package com.example.dinus.bottomnavbar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.ArticleAdapter
import com.example.dinus.adapter.VideoAdapter
import com.example.dinus.feature.anonymousChat.AnonymousChatActivity
import com.example.dinus.feature.anonymousChat.WelcomeAnonymousChatActivity
import com.example.dinus.feature.detectionTest.DetectionTestActivity
import com.example.dinus.feature.detectionTest.WelcomeDetectionTestActivity
import com.example.dinus.feature.event.EventActivity
import com.example.dinus.feature.event.WelcomeEventActivity
import com.example.dinus.feature.meditation.WelcomeMeditationActivity
import com.example.dinus.feature.moodDetection.WelcomeMoodDetectionActivity
import com.example.dinus.feature.music.WelcomeMusicActivity
import com.example.dinus.feature.quotes.WelcomeQuotesActivity
import com.example.dinus.feature.recovery.WelcomeRecoveryPlanActivity
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.Article
import com.example.dinus.model.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var artikelAdapter: ArticleAdapter
    private var artikelList: MutableList<Article> = mutableListOf()

    private lateinit var databaseReferenceArtickel: DatabaseReference

    private lateinit var videoAdapter: VideoAdapter
    private var videoList: MutableList<Video> = mutableListOf()

    private lateinit var databaseReferenceVideo: DatabaseReference

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val layoutDetectionTest: LinearLayout = view.findViewById(R.id.layoutDetectionTest)
        val layoutAnonymousChat: LinearLayout = view.findViewById(R.id.layoutAnonymousChat)
        val layoutEvent: LinearLayout = view.findViewById(R.id.layoutEvent)
        val layoutQuote: LinearLayout = view.findViewById(R.id.layoutQuote)
        val layoutMusic: LinearLayout = view.findViewById(R.id.layoutMusic)
        val layoutRecovery: LinearLayout = view.findViewById(R.id.layoutRecovery)
        val layoutMeditation: LinearLayout = view.findViewById(R.id.layoutMeditation)
        val layoutMood: LinearLayout = view.findViewById(R.id.layoutMood)

        layoutDetectionTest.setOnClickListener {
            startActivity(Intent(context, WelcomeDetectionTestActivity::class.java))
        }

        layoutRecovery.setOnClickListener {
            startActivity(Intent(context, WelcomeRecoveryPlanActivity::class.java))
        }

        layoutQuote.setOnClickListener {
            startActivity(Intent(context, WelcomeQuotesActivity::class.java))
        }

        layoutAnonymousChat.setOnClickListener {
            startActivity(Intent( context, WelcomeAnonymousChatActivity::class.java))
        }

        layoutEvent.setOnClickListener {
            startActivity(Intent(context, WelcomeEventActivity::class.java))
        }

        layoutMusic.setOnClickListener {
            startActivity(Intent(context, WelcomeMusicActivity::class.java))
        }

        layoutMeditation.setOnClickListener {
            startActivity(Intent(context, WelcomeMeditationActivity::class.java))
        }

        layoutMood.setOnClickListener {
            startActivity(Intent(context, WelcomeMoodDetectionActivity::class.java))
        }

        val rvArticle: RecyclerView = view.findViewById(R.id.rvArticle)
        rvArticle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        artikelAdapter = ArticleAdapter(artikelList)
        rvArticle.adapter = artikelAdapter

        databaseReferenceArtickel = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("article")

        fecthDataArticle()

        val rvVideo: RecyclerView = view.findViewById(R.id.rvVideo)

        rvVideo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        videoAdapter = VideoAdapter(videoList)
        rvVideo.adapter = videoAdapter

        databaseReferenceVideo = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("video")

        fetchDataVideo()

        return view
    }

    private fun fecthDataArticle() {
        databaseReferenceArtickel.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                artikelList.clear()
                for (articleSnasphot in dataSnapshot.children) {
                    val article = articleSnasphot.getValue(Article::class.java)
                    article?.let { artikelList.add(it) }

                }
                artikelAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun fetchDataVideo() {
        databaseReferenceVideo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                videoList.clear()
                for (videoSnapshot in dataSnapshot.children) {
                    val video = videoSnapshot.getValue(Video::class.java)
                    video?.let { videoList.add(it) }
                }
                videoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }

}