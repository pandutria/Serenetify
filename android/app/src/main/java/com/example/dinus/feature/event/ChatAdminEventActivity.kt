package com.example.dinus.feature.event

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.ChatEventAdapter
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.EventMessege
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatAdminEventActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etChat: EditText
    private lateinit var btnSend: ImageView
    private lateinit var chatEventAdapter: ChatEventAdapter
    private val chatMessages = mutableListOf<EventMessege>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fetchMessagesRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_admin_event)
        enableEdgeToEdge()


        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val senderId = sharedPreferencesHelper.getUserId()
        val adminId = intent.getIntExtra("adminId", -1)

        rvChat = findViewById(R.id.rvChat)
        etChat = findViewById(R.id.etChat)
        btnSend = findViewById(R.id.btnSend)

        rvChat.layoutManager = LinearLayoutManager(this)
        chatEventAdapter = ChatEventAdapter(chatMessages, senderId)
        rvChat.adapter = chatEventAdapter

        fetchChatMessages(adminId, senderId)

        startMessagePolling(adminId, senderId)

        btnSend.setOnClickListener {
            val message = etChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(senderId, adminId, message)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChatMessages(adminId: Int, senderId: Int) {
        apiService.getChatEventMessages(adminId, senderId).enqueue(object : Callback<List<EventMessege>> {
            override fun onResponse(call: Call<List<EventMessege>>, response: Response<List<EventMessege>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.clear()
                        chatMessages.addAll(it)
                        chatEventAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ChatAdminEvent", "Error fetching messages: ${response.code()} ${response.message()}")
                    Toast.makeText(this@ChatAdminEventActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EventMessege>>, t: Throwable) {
                Log.e("ChatAdminEvent", "Error fetching messages: ${t.message}", t)
                Toast.makeText(this@ChatAdminEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(senderId: Int, adminId: Int, message: String) {
        val eventMessage = EventMessege(sender_id = senderId, receiver_id = adminId, message = message)
        apiService.sendMessage(eventMessage).enqueue(object : Callback<EventMessege> {
            override fun onResponse(call: Call<EventMessege>, response: Response<EventMessege>) {
                if (response.isSuccessful) {
                    etChat.text.clear()
                    fetchChatMessages(adminId, senderId) // Fetch new messages after sending
                } else {
                    Log.e("ChatAdminEvent", "Error sending message: ${response.code()} ${response.message()}")
                    Toast.makeText(this@ChatAdminEventActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventMessege>, t: Throwable) {
                Log.e("ChatAdminEvent", "Error sending message: ${t.message}", t)
                Toast.makeText(this@ChatAdminEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Start polling for new messages every 1 second
    private fun startMessagePolling(adminId: Int, senderId: Int) {
        fetchMessagesRunnable = object : Runnable {
            override fun run() {
                fetchChatMessages(adminId, senderId)
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
