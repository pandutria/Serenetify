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
import com.example.dinus.feature.article.DetailArticleActivity
import com.example.dinus.model.Article

class ArticleAdapter (private val articleList: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitel: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val img: ImageView = itemView.findViewById(R.id.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]
        holder.tvTitel.text = article.title
        holder.tvDesc.text = article.description

        Glide.with(holder.itemView.context)
            .load(article.image)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val i = Intent(holder.itemView.context, DetailArticleActivity::class.java)
            i.putExtra("link", article.link)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}