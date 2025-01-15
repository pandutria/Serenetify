package com.example.dinus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.model.PostForYou

class PostAdapter(private val postList: List<PostForYou>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.img_url)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMinute: TextView = itemView.findViewById(R.id.tvMinute)
        val img: ImageView = itemView.findViewById(R.id.img_url)
        val tvText: TextView = itemView.findViewById(R.id.tvText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_for_you, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.tvName.text = post.name
        holder.tvMinute.text = post.minute
        holder.tvText.text = post.text

        Glide.with(holder.itemView.context)
            .load(post.profile_url)
            .into(holder.imgProfile)

        Glide.with(holder.itemView.context)
            .load(post.img_url)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
