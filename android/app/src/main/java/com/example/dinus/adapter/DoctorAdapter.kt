package com.example.dinus.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dinus.R
import com.example.dinus.feature.consultation.ChatDoctorActivity
import com.example.dinus.model.Doctor
import de.hdodenhof.circleimageview.CircleImageView

class DoctorAdapter(private val doctorList: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDoctor: CircleImageView = itemView.findViewById(R.id.image)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val btnChat: Button = itemView.findViewById(R.id.btnChatNow)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.tvName.text = doctor.user.name
//        holder.tvId.text = doctor.user.id.toString()

        Glide.with(holder.itemView.context)
                .load(doctor.image_url)
                .into(holder.imgDoctor)

        holder.btnChat.setOnClickListener {
            val i = Intent(holder.itemView.context, ChatDoctorActivity::class.java)
            i.putExtra("doctor_name", doctor.user.name)
            i.putExtra("doctor_id", doctor.user.id)
            i.putExtra("doctor_image", doctor.image_url)
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }
}
