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
import com.example.dinus.model.Quote
import de.hdodenhof.circleimageview.CircleImageView

class QuoteAdapter(private val quoteList: List<Quote>) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMinute: TextView = itemView.findViewById(R.id.tvMinute)
        val tvQuote: TextView = itemView.findViewById(R.id.tvQuote)
        val tvPencipta: TextView = itemView.findViewById(R.id.tvPencipta)
        val img: CircleImageView = itemView.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quoteList[position]
        holder.tvName.text = quote.name
        holder.tvMinute.text = quote.minute
        holder.tvQuote.text = quote.quote
        holder.tvPencipta.text = quote.pencipta

        Glide.with(holder.itemView.context)
            .load(quote.img)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }
}
