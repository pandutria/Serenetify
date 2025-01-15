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
import com.example.dinus.feature.music.DetailMusicActivity
import com.example.dinus.model.Music

class MusicAdapter(private val musicList: List<Music>) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPencipta: TextView = itemView.findViewById(R.id.tvPencipta)  
        val image: ImageView = itemView.findViewById(R.id.image)
        val btnPlay: ImageView = itemView.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music_one, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.tvName.text = music.name
        holder.tvPencipta.text = music.pencipta

        Glide.with(holder.itemView.context)
            .load(music.image)
            .into(holder.image)

        holder.btnPlay.setOnClickListener {
            val i = Intent(holder.itemView.context, DetailMusicActivity::class.java)
            i.putExtra("name", music.name)
            i.putExtra("pencipta", music.pencipta)
            i.putExtra("image", music.image)
            i.putExtra("name", music.name)
            i.putExtra("duration", music.total_duration)
            i.putExtra("music", music.music)

            holder.itemView.context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}
