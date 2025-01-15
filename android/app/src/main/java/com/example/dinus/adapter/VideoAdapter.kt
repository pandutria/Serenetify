package com.example.dinus.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.feature.video.DetailVideoActivity
import com.example.dinus.model.Video

class VideoAdapter(private val videoList: List<Video>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val img: ImageView = itemView.findViewById(R.id.img)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.tvTitle.text = video.title
        holder.tvDesc.text = video.desc

        Glide.with(holder.itemView.context)
            .load(video.image)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailVideoActivity::class.java)
            intent.putExtra("link", video.link)
            intent.putExtra("desc", video.desc)
            intent.putExtra("title", video.title)
            intent.putExtra("source", video.source)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}
