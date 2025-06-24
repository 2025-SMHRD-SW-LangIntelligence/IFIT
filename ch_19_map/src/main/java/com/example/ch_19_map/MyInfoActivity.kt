package com.example.ch_19_map

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton

class MyInfoActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnBack: Button
    private lateinit var btnMyPosts: Button
    private lateinit var etPassword: EditText
    private lateinit var btnTogglePassword: ImageButton
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        initializeViews()
        loadUserInfo()
        setupListeners()
        btnMyPosts = findViewById(R.id.btnMyPosts)
        btnMyPosts.setOnClickListener {
            val intent = Intent(this, MyPostsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        etPassword = findViewById(R.id.etPassword)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun loadUserInfo() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "이름 없음")
        val email = sharedPreferences.getString("email", "이메일 없음")
        val password = sharedPreferences.getString("password", "비밀번호 없음")

        tvName.text = "이름: $username"
        tvEmail.text = "이메일: $email"
        etPassword.setText(password)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // 비밀번호 표시/숨김 토글 기능
        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnTogglePassword.setImageResource(R.drawable.ic_visibility)
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePassword.setImageResource(R.drawable.ic_visibility_off)
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }
} 
