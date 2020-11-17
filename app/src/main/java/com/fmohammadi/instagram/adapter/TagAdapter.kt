package com.fmohammadi.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmohammadi.instagram.R

class TagAdapter(
    val mContext: Context,
    var mTags: List<String>,
    var mTagsCount: List<String>
) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag = itemView.findViewById<TextView>(R.id.hash_tag)
        var noOfPosts = itemView.findViewById<TextView>(R.id.no_of_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagAdapter.ViewHolder, position: Int) {
        holder.apply {
            tag.text = mTags[position]
            noOfPosts.text = mTagsCount[position]
        }
    }

    override fun getItemCount(): Int {
        return mTags.size
    }

    fun filter(
        filterTags: ArrayList<String>,
        filterTagsCount: ArrayList<String>
    ) {
        mTags = filterTags
        mTagsCount = filterTagsCount
        notifyDataSetChanged()
    }
}