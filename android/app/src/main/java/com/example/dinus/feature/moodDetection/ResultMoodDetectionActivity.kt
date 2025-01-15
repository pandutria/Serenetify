package com.example.dinus.feature.moodDetection

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dinus.MainActivity
import com.example.dinus.R
import com.example.dinus.feature.anonymousChat.WelcomeAnonymousChatActivity
import com.example.dinus.feature.meditation.WelcomeMeditationActivity
import com.example.dinus.feature.music.WelcomeMusicActivity
import com.example.dinus.feature.quotes.WelcomeQuotesActivity
import com.example.dinus.model.Mood
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ResultMoodDetectionActivity : AppCompatActivity() {
    private lateinit var tvMood: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_mood_detection)

        tvMood = findViewById(R.id.tvMood)

        findViewById<LinearLayout>(R.id.layoutDetectionTest).setOnClickListener {
            startActivity(Intent(this, WelcomeMoodDetectionActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutRelaxationMusic).setOnClickListener {
            startActivity(Intent(this, WelcomeMusicActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutMeditation).setOnClickListener {
            startActivity(Intent(this, WelcomeMeditationActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutRelaxationMusic).setOnClickListener {
            startActivity(Intent(this, WelcomeAnonymousChatActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutRelaxationMusic).setOnClickListener {
            startActivity(Intent(this, WelcomeQuotesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.layoutConsultation).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToConsultation", true)
            startActivity(intent)
            finish()
        }

        val pieChart: PieChart = findViewById(R.id.pieChart)

        val mood = intent.getParcelableExtra<Mood>("mood")
        val text = intent.getStringExtra("text")

        if (mood != null && text != null) {
            displayMoodData(pieChart, mood)
        }
    }

    private fun displayMoodData(pieChart: PieChart, mood: Mood) {

        val entries = listOf(
            PieEntry(mood.anger.toFloat(), "Anger"),
            PieEntry(mood.fear.toFloat(), "Fear"),
            PieEntry(mood.happy.toFloat(), "Happy"),
            PieEntry(mood.love.toFloat(), "Love"),
            PieEntry(mood.sadness.toFloat(), "Sadness"),
            PieEntry(mood.surprise.toFloat(), "Surprise")
        )

        val colors = ColorTemplate.COLORFUL_COLORS.toList()

        val highestEntry = entries.maxByOrNull { it.value } ?: entries[0]
        val highestLabel = highestEntry.label
        val highestValue = highestEntry.value.toInt()
        val highestColor = colors[entries.indexOf(highestEntry)]

        tvMood.text = "Your Mood is " + highestLabel

        val dataSet = PieDataSet(entries, "Mood Distribution")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val pieData = PieData(dataSet)

        pieChart.setUsePercentValues(true)

        pieChart.data = pieData

        pieChart.description.isEnabled = false
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setDrawEntryLabels(true)
        pieChart.legend.isEnabled = false
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)
        pieChart.centerText = "$highestLabel\n$highestValue%"
        pieChart.setCenterTextSize(18f)
        pieChart.setCenterTextColor(highestColor)

        pieChart.invalidate()
    }



}