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
                                
                                // 로그인 성공 시 사용자 정보를 SharedPreferences에 저장
                                // 실제 사용자 정보는 서버에서 제공되지 않으므로 기본값 사용
                                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                prefs.edit()
                                    .putLong("user_id", 1L) // 기본 사용자 ID
                                    .putString("username", email.split("@")[0]) // 이메일에서 사용자명 추출
                                    .putString("email", email)
                                    .putString("role", "USER")
                                    .putString("password", password)
                                    .apply()
                                
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