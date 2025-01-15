package com.example.dinus.feature.detectionTest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dinus.MainActivity
import com.example.dinus.R

class ResultDetectionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_detection_test)

        findViewById<Button>(R.id.btnHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.btnConsultation).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToConsultation", true)
            startActivity(intent)
            finish()
        }

        val tvTotalPoint: TextView = findViewById(R.id.tvTotalPoint)
        val tvRiskLevel: TextView = findViewById(R.id.tvRiskLevel)
        val tvDesc: TextView = findViewById(R.id.tvDesc)

        val totalPoints = intent.extras?.getInt("totalPoints", 0) ?: 0

        tvTotalPoint.text = totalPoints.toString()

        val (riskLevel, riskDescription) = getRiskDescription(totalPoints)

        tvRiskLevel.text = riskLevel
        tvDesc.text = riskDescription
    }

    fun getRiskDescription(totalPoints: Int): Pair<String, String> {
        return when {
            totalPoints <= 6 -> Pair(
                "Low ",
                "You appear to have a low level of stress or related symptoms. While occasional stress is normal, maintaining healthy habits and a balanced lifestyle can help you stay resilient. If you notice any changes in your well-being, consider monitoring your stress levels."
            )
            totalPoints <= 11 -> Pair(
                "Moderate",
                "You may be experiencing moderate stress or related symptoms. This is a sign to pay attention to your mental health and consider adopting stress management techniques, such as relaxation exercises, mindfulness, or talking to someone you trust. Seeking early guidance can prevent potential escalation."
            )
            else -> Pair(
                "High",
                "Your responses indicate a high level of stress or significant symptoms that may affect your daily life. It is important to seek professional support or counseling to address these challenges. Remember, taking action early can greatly improve your mental health and overall well-being."
            )
        }
    }
}
