package com.example.dinus.feature.anonymousChat

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.AnonymousChatAdapter
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.Message
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatingAnonymousChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etChat: EditText
    private lateinit var btnSend: ImageView
    private lateinit var chatAdapter: AnonymousChatAdapter
    private val chatMessages = mutableListOf<Message>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    private val handler = Handler()
    private val pollingRunnable = object : Runnable {
        override fun run() {
            val senderId = SharedPreferencesHelper(this@ChatingAnonymousChatActivity).getUserId()
            val receiverId = intent.getIntExtra("id", -1)
            fetchMessages(senderId, receiverId)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chating_anonymous_chat)
        enableEdgeToEdge()

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val senderId = sharedPreferencesHelper.getUserId()
        val receiverId = intent.getIntExtra("id", -1)

        findViewById<TextView>(R.id.tvId).text = receiverId.toString()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        rvChat = findViewById(R.id.rvChat)
        etChat = findViewById(R.id.etChat)
        btnSend = findViewById(R.id.btnSend)

        rvChat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatAdapter = AnonymousChatAdapter(chatMessages, senderId)
        rvChat.adapter = chatAdapter

        // Start polling for messages
        handler.post(pollingRunnable)

        btnSend.setOnClickListener {
            val message = etChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(senderId, receiverId, message)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchMessages(senderId: Int, receiverId: Int) {
        apiService.getChatMessages(receiverId, senderId).enqueue(object : Callback<List<Message>> {
            override fun onResponse(
                call: Call<List<Message>>,
                response: Response<List<Message>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.clear()
                        chatMessages.addAll(it)
                        chatAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ChatingAnonymousChatActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Toast.makeText(this@ChatingAnonymousChatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(senderId: Int, receiverId: Int, message: String) {
        val chatMessage = Message(sender_id = senderId, receiver_id = receiverId, message = message)
        apiService.sendMessage(chatMessage).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    etChat.text.clear()
                    fetchMessages(senderId, receiverId)
                } else {
                    Toast.makeText(this@ChatingAnonymousChatActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(this@ChatingAnonymousChatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(pollingRunnable) // Stop polling when activity is destroyed
    }
}
