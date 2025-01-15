package com.example.dinus.feature.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.MeditationAdapter
import com.example.dinus.model.Meditation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MeditationActivity : AppCompatActivity() {

    private lateinit var meditationAdapter: MeditationAdapter
    private var meditationList: MutableList<Meditation> = mutableListOf()

    private lateinit var databaseReferenceMeditation: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)
        enableEdgeToEdge()

        val linearLayoutMind: LinearLayout = findViewById(R.id.linearLayoutMind)
        val linearLayoutRelax: LinearLayout = findViewById(R.id.linearLayoutRelax)
        val linearLayoutSleep: LinearLayout = findViewById(R.id.linearLayoutSleep)
        val linearLayoutFocus: LinearLayout = findViewById(R.id.linearLayoutFocus)

        linearLayoutMind.setOnClickListener {
            val i = Intent(this, MeditationCategoryActivity::class.java)
            i.putExtra("category", "Mind")
            startActivity(i)
        }

        linearLayoutRelax.setOnClickListener {
            val i = Intent(this, MeditationCategoryActivity::class.java)
            i.putExtra("category", "Relax")
            startActivity(i)
        }

        linearLayoutSleep.setOnClickListener {
            val i = Intent(this, MeditationCategoryActivity::class.java)
            i.putExtra("category", "Sleep")
            startActivity(i)
        }

        linearLayoutFocus.setOnClickListener {
            val i = Intent(this, MeditationCategoryActivity::class.java)
            i.putExtra("category", "Focus")
            startActivity(i)
        }

        val rvMeditation: RecyclerView = findViewById(R.id.tvMeditation)
        rvMeditation.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        meditationAdapter = MeditationAdapter(meditationList)
        rvMeditation.adapter = meditationAdapter

        databaseReferenceMeditation = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("meditation")

        fetchDataMeditation()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun fetchDataMeditation() {
        databaseReferenceMeditation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                meditationList.clear()
                for (meditationSnapshot in dataSnapshot.children) {
                    val meditation = meditationSnapshot.getValue(Meditation::class.java)
                    meditation?.let { meditationList.add(it) }
                }
                meditationAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }
}