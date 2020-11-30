package com.fmohammadi.instagram.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.adapter.PostAdapter
import com.fmohammadi.instagram.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList: ArrayList<Post>? = null
    private var followingList: ArrayList<String>? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.recycler_view_posts.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager!!.stackFromEnd = true
        linearLayoutManager!!.reverseLayout = true
        view.recycler_view_posts.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = PostAdapter(context!!, postList!!)
        view.recycler_view_posts.adapter = postAdapter

        followingList = ArrayList()
        checkFollowingUsers()

        return view
    }

    private fun checkFollowingUsers() {
        FirebaseDatabase.getInstance().reference.child("Follow")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("following").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    followingList!!.clear();
                    for (snapshot in dataSnapshot.children) {
                        snapshot.key?.let { followingList!!.add(it) }
                    }
                    readPosts()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun readPosts(){
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList!!.clear()
                for (snapshot in dataSnapshot.getChildren()) {
                    val post = snapshot.getValue<Post>(Post::class.java)
                    for (id in followingList!!) {
                        if (post!!.publisher == id) {
                            postList!!.add(post)
                        }
                    }
                }
                postAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}