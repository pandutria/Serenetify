package com.example.dinus.feature.community

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.ChatCommunityAdapter
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.CommunityMessage
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide

class ChatCommunityActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etChat: EditText
    private lateinit var btnSend: ImageView
    private lateinit var chatCommunityAdapter: ChatCommunityAdapter
    private val chatMessages = mutableListOf<CommunityMessage>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private lateinit var handler: Handler
    private lateinit var pollingRunnable: Runnable
    private val pollingInterval: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_community)
        enableEdgeToEdge()

        findViewById<TextView>(R.id.tvName).text = intent.getStringExtra("name")
        findViewById<TextView>(R.id.tvDesc).text = intent.getStringExtra("desc")
        val img: ImageView = findViewById(R.id.image)

        Glide.with(this)
            .load(intent.getStringExtra("img"))
            .into(img)

        val communityId = intent.getIntExtra("community_id", -1)
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val senderId = sharedPreferencesHelper.getUserId()

        rvChat = findViewById(R.id.rvChat)
        etChat = findViewById(R.id.etChat)
        btnSend = findViewById(R.id.btnSend)

        rvChat.layoutManager = LinearLayoutManager(this)
        chatCommunityAdapter = ChatCommunityAdapter(chatMessages, senderId)
        rvChat.adapter = chatCommunityAdapter

        handler = Handler(Looper.getMainLooper())
        pollingRunnable = object : Runnable {
            override fun run() {
                fetchChatMessages(communityId, senderId)
                handler.postDelayed(this, pollingInterval)
            }
        }

        startPolling()

        btnSend.setOnClickListener {
            val message = etChat.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(senderId, communityId, message)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun fetchChatMessages(communityId: Int, senderId: Int) {
        apiService.getCommunityMessages(communityId, senderId).enqueue(object : Callback<List<CommunityMessage>> {
            override fun onResponse(call: Call<List<CommunityMessage>>, response: Response<List<CommunityMessage>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.clear()
                        chatMessages.addAll(it)
                        chatCommunityAdapter.notifyDataSetChanged()
                        rvChat.scrollToPosition(chatMessages.size - 1)
                    }
                } else {
                    Toast.makeText(this@ChatCommunityActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CommunityMessage>>, t: Throwable) {
                Toast.makeText(this@ChatCommunityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(senderId: Int, communityId: Int, message: String) {
        val communityMessage = CommunityMessage(
            sender_id = senderId,
            community_id = communityId,
            message = message,
            timestamp = System.currentTimeMillis().toString()
        )
        apiService.sendCommunityMessage(communityMessage).enqueue(object : Callback<CommunityMessage> {
            override fun onResponse(call: Call<CommunityMessage>, response: Response<CommunityMessage>) {
                if (response.isSuccessful) {
                    etChat.text.clear()
                    fetchChatMessages(communityId, senderId)
                } else {
                    Toast.makeText(this@ChatCommunityActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommunityMessage>, t: Throwable) {
                Toast.makeText(this@ChatCommunityActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startPolling() {
        handler.post(pollingRunnable)
    }

    private fun stopPolling() {
        handler.removeCallbacks(pollingRunnable)
    }

    override fun onPause() {
        super.onPause()
        stopPolling()
    }

    override fun onResume() {
        super.onResume()
        startPolling()
    }
}