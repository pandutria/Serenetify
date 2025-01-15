package com.example.dinus.feature.meditation

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.MeditationCategoryAdapter
import com.example.dinus.model.Meditation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MeditationCategoryActivity : AppCompatActivity() {

    private lateinit var meditationCategoryAdapter: MeditationCategoryAdapter
    private var meditationList: MutableList<Meditation> = mutableListOf()

    private lateinit var databaseReferenceMeditation: DatabaseReference
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meditation_category)


        selectedCategory = intent.getStringExtra("category")

        val rvMeditationCategory: RecyclerView = findViewById(R.id.rvMedtitationCategory)
        rvMeditationCategory.layoutManager = LinearLayoutManager(this)
        meditationCategoryAdapter = MeditationCategoryAdapter(meditationList)
        rvMeditationCategory.adapter = meditationCategoryAdapter

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

                    if (meditation != null && meditation.category == selectedCategory) {
                        meditationList.add(meditation)
                    }
                }
                meditationCategoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}