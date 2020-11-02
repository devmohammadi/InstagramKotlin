package com.fmohammadi.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.model.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    val mUsers: ArrayList<User>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageProfile = itemView.findViewById<CircleImageView>(R.id.image_profile)
        var userName = itemView.findViewById<TextView>(R.id.username)
        var fullName = itemView.findViewById<TextView>(R.id.fullName)
        var btnFollow = itemView.findViewById<Button>(R.id.btn_follow)

        fun bindView(user: User) {
            userName.text = user.username
            fullName.text = user.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(mUsers[position])
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}