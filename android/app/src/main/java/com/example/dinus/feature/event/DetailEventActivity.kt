package com.example.dinus.feature.event

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.model.Event
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvOrganizer: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvAdminName: TextView
    private lateinit var imgEvent: ImageView
    private lateinit var tvId:TextView
    private lateinit var tvTime:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_event)

        tvName = findViewById(R.id.tvName)
        tvOrganizer = findViewById(R.id.tvOrganizer)
        tvLocation = findViewById(R.id.tvLocation)
        tvPrice = findViewById(R.id.tvPrice)
        tvDesc = findViewById(R.id.tvDesc)
        tvDate = findViewById(R.id.tvDate)
        tvCity = findViewById(R.id.tvCity)
        tvAdminName = findViewById(R.id.tvAdminName)
        imgEvent = findViewById(R.id.image)
        tvId = findViewById(R.id.tvId)
        tvTime = findViewById(R.id.tvTime)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val eventId = intent.getIntExtra("id", -1)

        if (eventId != -1) {
            fetchEventDetails(eventId)
        } else {
            Toast.makeText(this, "Invalid event ID", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnChatAdmin).setOnClickListener {
            val adminId = tvId.text.toString().toIntOrNull()
            val i = Intent(this, ChatAdminEventActivity::class.java)
            i.putExtra("adminId", adminId)
            startActivity(i)
        }
    }

    private fun fetchEventDetails(eventId: Int) {
        // Initialize Retrofit
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getEventById(eventId).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()

                    event?.let {
                        tvName.text = it.name
                        tvOrganizer.text = it.organizer
                        tvLocation.text = it.location
                        tvPrice.text = "Rp " + it.price
                        tvDesc.text = it.description
                        tvDate.text = it.date
                        tvTime.text = it.time + " WIB"
                        tvCity.text = it.city
                        tvAdminName.text = it.admin.name
                        tvId.text = it.admin.id.toString()

                        Glide.with(this@DetailEventActivity)
                            .load(it.image_url)
                            .into(imgEvent)
                    }
                } else {
                    Toast.makeText(this@DetailEventActivity, "Failed to load event", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Toast.makeText(this@DetailEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

