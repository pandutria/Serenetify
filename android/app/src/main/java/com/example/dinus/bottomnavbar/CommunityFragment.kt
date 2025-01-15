package com.example.dinus.bottomnavbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.CommunityAdapter
import com.example.dinus.adapter.PostAdapter
import com.example.dinus.model.Community
import com.example.dinus.model.PostForYou
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {

    private lateinit var rvCommunity: RecyclerView
    private lateinit var communityAdapter: CommunityAdapter
    private val communityList = mutableListOf<Community>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    private lateinit var rvPost: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PostForYou>()
    private lateinit var databaseReferencePost: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_community, container, false)


        rvPost = view.findViewById(R.id.rvPost)
        postAdapter = PostAdapter(postList)
        rvPost.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvPost.adapter = postAdapter

        databaseReferencePost = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("post")

        fetchCommunitiesFromFirebase()


        rvCommunity = view.findViewById(R.id.rvCommunity)
        communityAdapter = CommunityAdapter(communityList)

        rvCommunity.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCommunity.adapter = communityAdapter

        fetchCommunitiesFromApi()

        return view
    }

    private fun fetchCommunitiesFromApi() {
        apiService.getCommunities().enqueue(object : Callback<List<Community>> {
            override fun onResponse(call: Call<List<Community>>, response: Response<List<Community>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        communityList.clear()
                        communityList.addAll(it)
                        communityAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load communities", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Community>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCommunitiesFromFirebase() {
        databaseReferencePost.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(posSnapshot: DataSnapshot) {
                postList.clear()
                for (postSnapshot in posSnapshot.children) {
                    val post = postSnapshot.getValue(PostForYou::class.java)
                    post?.let { postList.add(it) }
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
