package com.example.dinus.feature.event

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.EventAdapter
import com.example.dinus.model.Event
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventActivity : AppCompatActivity() {

    private lateinit var rvEvent: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val eventList = mutableListOf<Event>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event)

        rvEvent = findViewById(R.id.rvEvent)
        eventAdapter = EventAdapter(eventList)

        rvEvent.layoutManager = LinearLayoutManager(this)
        rvEvent.adapter = eventAdapter

        fetchDataFromApi()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }


    }

    private fun fetchDataFromApi() {
        Log.d("FetchData", "Fetching events from API...")
        apiService.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                Log.d("FetchData", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    Log.d("FetchData", "Events fetched: $events")

                    if (events.isNotEmpty()) {
                        eventList.clear()
                        eventList.addAll(events)
                        eventAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("FetchData", "No events found")
                        Toast.makeText(this@EventActivity, "No events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FetchData", "Failed to load events: ${response.code()}")
                    Toast.makeText(this@EventActivity, "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e("FetchData", "Error: ${t.message}")
                Toast.makeText(this@EventActivity, "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}