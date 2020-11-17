package com.fmohammadi.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    val mContext: Context,
    val mUsers: ArrayList<User>,
    var isFragment: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageProfile = itemView.findViewById<CircleImageView>(R.id.image_profile)
        var userName = itemView.findViewById<TextView>(R.id.username)
        var fullName = itemView.findViewById<TextView>(R.id.fullName)
        var btnFollow = itemView.findViewById<Button>(R.id.btn_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val user = mUsers[position]

        holder.apply {
            btnFollow.visibility = View.VISIBLE
            userName.text = user.username
            fullName.text = user.name

            if(user.imageUrl != ""){
                Picasso.get()
                    .load(user.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageProfile)
            }


            isFollowed(user.uid, btnFollow)

            if (user.uid.equals(firebaseUser!!.uid))
                btnFollow.visibility = View.GONE
        }
    }

    private fun isFollowed(uid: String?, btnFollow: Button?) {
        val mRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Follow").child(firebaseUser!!.uid).child("following")

        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(uid!!).exists())
                    btnFollow!!.text = mContext.getString(R.string.following)
                else {
                    btnFollow!!.text = mContext.getString(R.string.follow)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}