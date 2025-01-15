package com.example.dinus.feature.recovery

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dinus.MainActivity
import com.example.dinus.R

class RecoveryPlanDoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recovery_plan_done)

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}