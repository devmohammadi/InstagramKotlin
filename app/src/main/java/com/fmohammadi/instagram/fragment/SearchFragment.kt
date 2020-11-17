package com.fmohammadi.instagram.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmohammadi.instagram.R
import com.fmohammadi.instagram.adapter.TagAdapter
import com.fmohammadi.instagram.adapter.UserAdapter
import com.fmohammadi.instagram.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private var mUsers: ArrayList<User>? = null
    private var userAdapter: UserAdapter? = null

    private var mHashTags: ArrayList<String>? = null
    private var mHashTagsCount: ArrayList<String>? = null
    private var tagAdapter: TagAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)

        view.recycler_view_users.setHasFixedSize(true)
        view.recycler_view_users.layoutManager = LinearLayoutManager(context)

        view.recycler_view_tags.setHasFixedSize(true)
        view.recycler_view_tags.layoutManager = LinearLayoutManager(context)

        mHashTags = ArrayList()
        mHashTagsCount = ArrayList()
        tagAdapter = TagAdapter(context!!, mHashTags!!, mHashTagsCount!!)
        view.recycler_view_tags.adapter = tagAdapter

        mUsers = ArrayList()
        userAdapter = UserAdapter(context!!, mUsers!!, true)
        view.recycler_view_users.adapter = userAdapter

        readUsers();
        readTags();

        view.search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchUser(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

        return view
    }

    private fun readTags() {
        FirebaseDatabase.getInstance().reference.child("HashTags")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mHashTags!!.clear()
                    mHashTagsCount!!.clear()

                    for (snapshot in dataSnapshot.children) {
                        mHashTags!!.add(snapshot.key!!)
                        mHashTagsCount!!.add(snapshot.childrenCount.toString() + "")
                    }
                    tagAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun readUsers() {
        FirebaseDatabase.getInstance().reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (TextUtils.isEmpty(view!!.search_bar.text.toString().trim())) {
                        mUsers!!.clear()

                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(User::class.java)
                            mUsers!!.add(user!!)
                        }
                        userAdapter!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun searchUser(s: String) {
        FirebaseDatabase.getInstance().reference.child("Users")
            .orderByChild("username").startAt(s).endAt(s + "\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mUsers!!.clear()

                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        mUsers!!.add(user!!)
                    }
                    userAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun filter(text: String) {
        val mSearchTags: ArrayList<String> = ArrayList()
        val mSearchTagsCount: ArrayList<String> = ArrayList()

        for (s in mHashTags!!) {
            if (s.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                mSearchTags.add(s)
                mSearchTagsCount.add(mHashTagsCount!![mHashTags!!.indexOf(s)])
            }
        }
        tagAdapter!!.filter(mSearchTags , mSearchTagsCount)
    }
}