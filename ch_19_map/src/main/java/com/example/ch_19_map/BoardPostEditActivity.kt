package com.example.ch_19_map

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class BoardPostEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_post_edit)

        val post = intent.getSerializableExtra("post") as? BoardPost
        val etTitle = findViewById<EditText>(R.id.etEditTitle)
        val etContent = findViewById<EditText>(R.id.etEditContent)
        val btnSave = findViewById<Button>(R.id.btnSaveEdit)

        post?.let {
            etTitle.setText(it.title)
            etContent.setText(it.content)
        }

        btnSave.setOnClickListener {
            val newTitle = etTitle.text.toString().trim()
            val newContent = etContent.text.toString().trim()
            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            post?.let {
                val request = BoardPostRequest(newTitle, newContent, it.author.id)
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.getApiService(this@BoardPostEditActivity).updateBoardPost(it.id, request)
                        if (response.isSuccessful) {
                            Toast.makeText(this@BoardPostEditActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@BoardPostEditActivity, "수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@BoardPostEditActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
} 