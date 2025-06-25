package com.example.ch_19_map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etId)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val request = UserLoginRequest(email, password)
                    val response = RetrofitClient.getApiService(this@LoginActivity).loginUser(request)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && responseBody.contains("로그인 성공")) {
                                Log.d("LoginActivity", "Login successful: $responseBody")
                                // 서버에서 JSON 형태로 사용자 정보를 반환한다고 가정
                                // { "id": 2, "username": "관리자", "email": "admin@parq.com", "role": "ADMIN" }
                                val loginResponse = try {
                                    Gson().fromJson(responseBody, LoginResponse::class.java)
                                } catch (e: Exception) {
                                    null
                                }
                                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                if (loginResponse != null) {
                                    prefs.edit()
                                        .putLong("user_id", loginResponse.id)
                                        .putString("username", loginResponse.username)
                                        .putString("email", loginResponse.email)
                                        .putString("role", loginResponse.role)
                                        .putString("password", password)
                                        .apply()
                                } else {
                                    // fallback: 기존 방식
                                    prefs.edit()
                                        .putLong("user_id", 1L)
                                        .putString("username", email.split("@")[0])
                                        .putString("email", email)
                                        .putString("role", "USER")
                                        .putString("password", password)
                                        .apply()
                                }
                                Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "로그인 실패: $responseBody", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("LoginActivity", "Network Error", e)
                        Toast.makeText(this@LoginActivity, "네트워크 오류: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        findViewById<TextView>(R.id.btnSignUp).setOnClickListener {
            // 회원가입 화면으로 이동
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
} 