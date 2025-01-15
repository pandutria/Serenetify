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
import com.example.dinus.feature.event.DetailEventActivity
import com.example.dinus.model.Event
import de.hdodenhof.circleimageview.CircleImageView

class EventAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvOrganizer: TextView = itemView.findViewById(R.id.tvOrganizer)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvAdminName: TextView = itemView.findViewById(R.id.tvAdminName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val imgEvent: ImageView = itemView.findViewById(R.id.imgUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        if (event != null) {
            holder.tvName.text = event.name
            holder.tvOrganizer.text = event.organizer
            holder.tvLocation.text = event.location
            holder.tvPrice.text = "Rp " + event.price
            holder.tvDate.text = event.date
            holder.tvCity.text = event.city
            holder.tvDesc.text = event.description
            holder.tvAdminName.text = event.admin.name
            holder.tvTime.text = event.time

            Glide.with(holder.itemView.context)
                .load(event.image_url)
                .into(holder.imgEvent)
        } else {
            holder.tvName.text = "No Data"
            holder.tvOrganizer.text = "No Data"
        }

        holder.itemView.setOnClickListener {
            val i = Intent(holder.itemView.context, DetailEventActivity::class.java)
            i.putExtra("id", event.id)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
