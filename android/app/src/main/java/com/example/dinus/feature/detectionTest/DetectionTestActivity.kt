package com.example.dinus.feature.detectionTest

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.TestDetectionAdapter
import com.example.dinus.model.TestDetection
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetectionTestActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var testDetectionAdapter: TestDetectionAdapter
    private val testList: MutableList<TestDetection> = mutableListOf()
    private val selectedPoints = mutableMapOf<Int, Int>()

    private lateinit var tvTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection_test)
        enableEdgeToEdge()

        tvTime = findViewById(R.id.tvTime)

        val btnDone: Button = findViewById(R.id.btnDone)
        btnDone.setOnClickListener {
            val totalPoints = selectedPoints.values.sum()
            val intent = Intent(this, ResultDetectionTestActivity::class.java).apply {
                putExtra("totalPoints", totalPoints)
            }
            startActivity(intent)
        }

        val rvTestDetection: RecyclerView = findViewById(R.id.rvTestDetection)
        rvTestDetection.layoutManager = LinearLayoutManager(this)

        testDetectionAdapter = TestDetectionAdapter(
            testList,
            onAnswerSelected = { position, points ->
                selectedPoints[position] = points
            }
        )
        rvTestDetection.adapter = testDetectionAdapter

        fetchDataFromFirebase()
        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        val totalTimeInMillis = 5 * 60 * 1000L // 5 menit dalam milidetik
        object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                tvTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                finish()
            }
        }.start()
    }

    private fun fetchDataFromFirebase() {
        database = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("test_detection")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testList.clear()
                for (dataSnapshot in snapshot.children) {
                    val testDetection = dataSnapshot.getValue(TestDetection::class.java)
                    testDetection?.let { testList.add(it) }
                }
                testDetectionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
