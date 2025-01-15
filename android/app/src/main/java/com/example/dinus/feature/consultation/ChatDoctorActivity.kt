package com.example.dinus.feature.consultation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.adapter.ChatDoctorAdapter
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.DoctorMessege
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatDoctorActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etChat: EditText
    private lateinit var btnSend: ImageView
    private lateinit var chatDoctorAdapter: ChatDoctorAdapter
    private val chatMessages = mutableListOf<DoctorMessege>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fetchMessagesRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_doctor)
        enableEdgeToEdge()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val tvName: TextView = findViewById(R.id.tvName)
        val tvId: TextView = findViewById(R.id.tvId)
        val image: CircleImageView = findViewById(R.id.image)

        val doctor_name = intent.getStringExtra("doctor_name")
        val doctor_id = intent.getIntExtra("doctor_id", 1)
        val doctor_image = intent.getStringExtra("doctor_image")

        tvName.text = doctor_name

        Glide.with(this)
            .load(doctor_image)
            .into(image)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val senderId = sharedPreferencesHelper.getUserId()
        val doctorId = intent.getIntExtra("doctor_id", -1)

        rvChat = findViewById(R.id.rvChat)
        etChat = findViewById(R.id.etChat)
        btnSend = findViewById(R.id.btnSend)

        rvChat.layoutManager = LinearLayoutManager(this)
        chatDoctorAdapter = ChatDoctorAdapter(chatMessages, senderId)
        rvChat.adapter = chatDoctorAdapter

        fetchChatMessages(doctorId, senderId)
        startMessagePolling(doctorId, senderId)

        btnSend.setOnClickListener {
            val message = etChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(senderId, doctorId, message)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChatMessages(doctorId: Int, senderId: Int) {
        apiService.getDoctorMessages(doctorId, senderId).enqueue(object : Callback<List<DoctorMessege>> {
            override fun onResponse(call: Call<List<DoctorMessege>>, response: Response<List<DoctorMessege>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.clear()
                        chatMessages.addAll(it)
                        chatDoctorAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ChatDoctorActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DoctorMessege>>, t: Throwable) {
                Toast.makeText(this@ChatDoctorActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(senderId: Int, doctorId: Int, message: String) {
        val doctorMessage = DoctorMessege(sender_id = senderId, receiver_id = doctorId, message = message, timestamp = System.currentTimeMillis().toString())
        apiService.sendMessage(doctorMessage).enqueue(object : Callback<DoctorMessege> {
            override fun onResponse(call: Call<DoctorMessege>, response: Response<DoctorMessege>) {
                if (response.isSuccessful) {
                    etChat.text.clear()
                    fetchChatMessages(doctorId, senderId) // Fetch new messages after sending
                } else {
                    Toast.makeText(this@ChatDoctorActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DoctorMessege>, t: Throwable) {
                Toast.makeText(this@ChatDoctorActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startMessagePolling(doctorId: Int, senderId: Int) {
        fetchMessagesRunnable = object : Runnable {
            override fun run() {
                fetchChatMessages(doctorId, senderId)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(fetchMessagesRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(fetchMessagesRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(fetchMessagesRunnable)
    }
}
