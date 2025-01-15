package com.example.dinus.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.feature.anonymousChat.ChatingAnonymousChatActivity
import com.example.dinus.model.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPhoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val btnChat: Button = itemView.findViewById(R.id.btnChat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_anonymous_chat, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val formattedName = formatName(user.name)
        holder.tvName.text = formattedName
//        holder.tvName.text = user.name
        holder.tvPhoneNumber.text = formatName( user.phone_number)

        holder.btnChat.setOnClickListener {
            val i = Intent(holder.itemView.context, ChatingAnonymousChatActivity::class.java)
            i.putExtra("name", user.name)
            i.putExtra("id", user.id)
            holder.itemView.context.startActivity(i)
        }
    }

    private fun formatName(name: String): String {
        return if (name.length > 1) {
            val firstChar = name.first()
            val lastChar = name.last()
            val maskedPart = "x".repeat(name.length - 2)
            "$firstChar$maskedPart$lastChar"
        } else {
            name
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}
