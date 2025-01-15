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
import com.example.dinus.feature.meditation.DetailMeditationActivity
import com.example.dinus.model.Meditation

class MeditationCategoryAdapter(private val meditationList: List<Meditation>) : RecyclerView.Adapter<MeditationCategoryAdapter.MeditationCategoryViewHolder>() {

    class MeditationCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val img: ImageView = itemView.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meditation_category, parent, false)
        return MeditationCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeditationCategoryViewHolder, position: Int) {
        val meditation = meditationList[position]
        holder.tvTitle.text = meditation.title
        holder.tvCategory.text = meditation.category

        Glide.with(holder.itemView.context)
            .load(meditation.image)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailMeditationActivity::class.java)
            intent.putExtra("title", meditation.title)
            intent.putExtra("desc", meditation.desc)
            intent.putExtra("category", meditation.category)
            intent.putExtra("video", meditation.video)
            intent.putExtra("image", meditation.image)
            intent.putExtra("source", meditation.source)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return meditationList.size
    }
}