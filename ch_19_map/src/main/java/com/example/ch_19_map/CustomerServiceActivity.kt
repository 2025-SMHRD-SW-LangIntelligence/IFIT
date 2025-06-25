package com.example.ch_19_map

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import android.content.Intent

class CustomerServiceActivity : AppCompatActivity() {

    private lateinit var boardPostAdapter: BoardPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)

        setupRecyclerView()
        fetchBoardPosts()

        findViewById<FloatingActionButton>(R.id.fabCreatePost).setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchBoardPosts() // 다른 화면에서 돌아왔을 때 목록을 새로고침
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvBoardPosts)
        boardPostAdapter = BoardPostAdapter(emptyList()) { post ->
            val intent = Intent(this, BoardPostDetailActivity::class.java)
            intent.putExtra("post", post)
            startActivity(intent)
        }
        recyclerView.adapter = boardPostAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchBoardPosts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getApiService(this@CustomerServiceActivity).getAllBoardPosts()
                if (response.isSuccessful) {
                    response.body()?.let { posts ->
                        boardPostAdapter.updatePosts(posts)
                    }
                } else {
                    Toast.makeText(this@CustomerServiceActivity, "게시글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CustomerServiceActivity", "Error fetching board posts", e)
                Toast.makeText(this@CustomerServiceActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 