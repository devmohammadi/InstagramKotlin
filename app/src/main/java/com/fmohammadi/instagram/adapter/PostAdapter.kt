package com.fmohammadi.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.model.Post
import com.fmohammadi.instagram.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hendraanggrian.appcompat.widget.SocialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class PostAdapter(
    val mContext: Context,
    val mPost: ArrayList<Post>,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageProfile = itemView.findViewById<CircleImageView>(R.id.profile_image)
        var postImage = itemView.findViewById<ImageView>(R.id.postImage)
        var like = itemView.findViewById<TextView>(R.id.like)
        var comment = itemView.findViewById<TextView>(R.id.comment)
        var save = itemView.findViewById<TextView>(R.id.save)
        var more = itemView.findViewById<TextView>(R.id.more)

        var userName = itemView.findViewById<TextView>(R.id.username)
        var noOfLike = itemView.findViewById<TextView>(R.id.no_of_like)
        var noOfComment = itemView.findViewById<TextView>(R.id.no_of_post)
        var author = itemView.findViewById<TextView>(R.id.author)

        var description = itemView.findViewById<SocialTextView>(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val post = mPost[position]
        Picasso.get().load(post.imageurl).into(holder.postImage)

        holder.description.text = post.description

        FirebaseDatabase.getInstance().reference.child("Users").child(post.publisher!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)
                    Picasso.get().load(user!!.imageUrl).into(holder.imageProfile)
                    holder.userName.text = user.username
                    holder.author.text = user.name
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun getItemCount(): Int {
       return mPost.size
    }

}