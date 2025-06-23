package com.example.ch_19_map

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val etTitle = findViewById<EditText>(R.id.etPostTitle)
        val etContent = findViewById<EditText>(R.id.etPostContent)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitPost)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()
            val userId = getUserId()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId == -1L) {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                // TODO: 로그인 화면으로 이동하는 로직 추가
                return@setOnClickListener
            }

            val request = BoardPostRequest(title, content, userId)
            createPost(request)
        }
    }

    private fun createPost(request: BoardPostRequest) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getApiService(this@CreatePostActivity).createBoardPost(request)
                if (response.isSuccessful) {
                    Toast.makeText(this@CreatePostActivity, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // 액티비티 종료
                } else {
                    if (response.code() == 404) {
                        Toast.makeText(this@CreatePostActivity, "작성자 정보를 찾을 수 없습니다. 다시 로그인해주세요.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@CreatePostActivity, "게시글 등록에 실패했습니다. (코드: ${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreatePostActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserId(): Long {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("user_id", -1L)
    }
} 