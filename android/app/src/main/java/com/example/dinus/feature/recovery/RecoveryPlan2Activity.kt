package com.example.dinus.feature.recovery

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dinus.R

class RecoveryPlan2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recovery_plan2)

        val cb1 = findViewById<CheckBox>(R.id.cb1)
        val cb2 = findViewById<CheckBox>(R.id.cb2)
        val cb3 = findViewById<CheckBox>(R.id.cb3)
        val cb4 = findViewById<CheckBox>(R.id.cb4)
        val cb5 = findViewById<CheckBox>(R.id.cb5)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnNext.setOnClickListener {
            if (cb1.isChecked && cb2.isChecked && cb3.isChecked && cb4.isChecked && cb5.isChecked) {
                val intent = Intent(this, RecoveryPlan3Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Harap centang semua opsi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onBackPressed() {
//        finishAffinity()
//        super.onBackPressed()
//    }
}