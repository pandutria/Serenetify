package com.example.dinus.adapter

import android.content.Intent
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.feature.community.ChatCommunityActivity
import com.example.dinus.model.Community
import de.hdodenhof.circleimageview.CircleImageView

class CommunityAdapter(private val communityList: List<Community>) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo_url: CircleImageView = itemView.findViewById(R.id.logo_url)
        val image_url: ImageView = itemView.findViewById(R.id.image_url)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val btnChat: Button = itemView.findViewById(R.id.btnJoin)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communityList[position]
        holder.tvName.text = community.name
        holder.tvDesc.text = community.description

        Glide.with(holder.itemView.context)
            .load(community.logo_url)
            .into(holder.logo_url)

        Glide.with(holder.itemView.context)
            .load(community.image_url)
            .into(holder.image_url)

        holder.btnChat.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatCommunityActivity::class.java)
            intent.putExtra("community_name", community.name)
            intent.putExtra("community_id", community.id)
            intent.putExtra("name", community.name)
            intent.putExtra("desc", community.description)
            intent.putExtra("img", community.image_url)

            holder.itemView.context.startActivity(intent)
        }

        val imageView = holder.itemView.findViewById<ImageView>(R.id.image_url)
        imageView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 15f)
            }
        }

        imageView.clipToOutline = true

    }

    override fun getItemCount(): Int = communityList.size
}
