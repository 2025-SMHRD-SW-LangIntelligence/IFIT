package com.example.ch_19_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardPostAdapter(
    private var posts: List<BoardPost>,
    private val onItemClick: (BoardPost) -> Unit
) : RecyclerView.Adapter<BoardPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_board_post, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<BoardPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, val onItemClick: (BoardPost) -> Unit) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.tvPostTitle)
        private val authorTextView: TextView = view.findViewById(R.id.tvPostAuthor)
        private var currentPost: BoardPost? = null

        init {
            view.setOnClickListener {
                currentPost?.let { onItemClick(it) }
            }
        }

        fun bind(post: BoardPost) {
            currentPost = post
            titleTextView.text = post.title
            authorTextView.text = post.author.username
        }
    }
} 