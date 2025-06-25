package com.example.ch_19_map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MyPostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        val rvMyPosts = findViewById<RecyclerView>(R.id.rvMyPosts)
        val adapter = BoardPostAdapter(emptyList()) { post ->
            val intent = Intent(this, BoardPostDetailActivity::class.java)
            intent.putExtra("post", post)
            startActivity(intent)
        }
        rvMyPosts.adapter = adapter
        rvMyPosts.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        if (userId != -1L) {
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.getApiService(this@MyPostsActivity).getBoardPostsByUser(userId)
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            adapter.updatePosts(posts)
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    }
} 