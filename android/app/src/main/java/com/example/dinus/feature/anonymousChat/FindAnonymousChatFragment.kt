package com.example.dinus.feature.anonymousChat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.UserAdapter
import com.example.dinus.local.SharedPreferencesHelper
import com.example.dinus.model.User
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindAnonymousChatFragment : Fragment() {

    private lateinit var rvFindUserAnonymousChat: RecyclerView
    private lateinit var apiService: ApiService
    private val userList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_anonymous_chat, container, false)

        rvFindUserAnonymousChat = view.findViewById(R.id.rvFindUserAnonymousChat)

        userAdapter = UserAdapter(userList)

        rvFindUserAnonymousChat.layoutManager = LinearLayoutManager(requireContext())
        rvFindUserAnonymousChat.adapter = userAdapter

        apiService = ApiClient.retrofit.create(ApiService::class.java)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        fetchDataFromApi()

        return view
    }

    private fun fetchDataFromApi() {
        Log.d("FetchData", "Fetching users from API...")
        val loggedInUserId = sharedPreferencesHelper.getUserId()

        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Log.d("FetchData", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    Log.d("FetchData", "Users fetched: $users")

                    val filteredUsers = users.filter { it.id != loggedInUserId }

                    if (filteredUsers.isNotEmpty()) {
                        userList.clear()
                        userList.addAll(filteredUsers)
                        userAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("FetchData", "No users found")
                        Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FetchData", "Failed to load users: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("FetchData", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
