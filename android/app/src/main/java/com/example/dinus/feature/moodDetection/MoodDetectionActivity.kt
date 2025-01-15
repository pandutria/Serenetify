package com.example.dinus.feature.moodDetection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dinus.R
import com.example.dinus.model.PredictRequest
import com.example.dinus.model.PredictResponse
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoodDetectionActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var btnDetection: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mood_detection)

        val etInputText: EditText = findViewById(R.id.etInputText)
        btnDetection = findViewById(R.id.btnDetection)
        progressBar = findViewById(R.id.loadingBar)

        progressBar.visibility = View.GONE

        btnDetection.setOnClickListener {
            val inputText = etInputText.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            btnDetection.visibility = View.GONE

            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            predictMood(inputText)
        }
    }

    private fun predictMood(text: String) {
        val request = PredictRequest(text)
        val apiService = ApiClient.retrofit2.create(ApiService::class.java)
        val call = apiService.predictMood(request)

        call.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    val predictResponse = response.body()
                    if (predictResponse != null) {
                        val intent = Intent(this@MoodDetectionActivity, ResultMoodDetectionActivity::class.java)
                        intent.putExtra("mood", predictResponse.mood)
                        intent.putExtra("text", predictResponse.text.user)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    progressBar.visibility = View.GONE
                    btnDetection.visibility = View.VISIBLE
                    Toast.makeText(this@MoodDetectionActivity, "Failed to get response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnDetection.visibility = View.VISIBLE
                Toast.makeText(this@MoodDetectionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}