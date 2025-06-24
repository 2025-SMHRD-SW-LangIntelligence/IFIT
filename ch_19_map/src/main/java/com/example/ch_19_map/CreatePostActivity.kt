package com.example.ch_19_map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CreatePostActivity : AppCompatActivity() {
    private val PICK_FILE_REQUEST_CODE = 101
    private val attachedFiles = mutableListOf<Uri>()
    private lateinit var attachmentPreviewLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val etTitle = findViewById<EditText>(R.id.etPostTitle)
        val etContent = findViewById<EditText>(R.id.etPostContent)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitPost)
        val btnAttachFile = findViewById<Button>(R.id.btnAttachFile)
        attachmentPreviewLayout = findViewById(R.id.attachmentPreviewLayout)

        btnAttachFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            val mimeTypes = arrayOf("image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

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

            uploadPostWithFiles(title, content, userId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.clipData != null) {
                    // 여러 개 선택
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val fileUri = data.clipData!!.getItemAt(i).uri
                        addAttachment(fileUri)
                    }
                } else if (data.data != null) {
                    // 한 개 선택
                    val fileUri = data.data!!
                    addAttachment(fileUri)
                }
            }
        }
    }

    private fun addAttachment(uri: Uri) {
        if (attachedFiles.size >= 5) {
            Toast.makeText(this, "최대 5개까지 첨부할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        attachedFiles.add(uri)
        val view = LayoutInflater.from(this).inflate(R.layout.item_attachment_preview, attachmentPreviewLayout, false)
        val ivPreview = view.findViewById<ImageView>(R.id.ivAttachmentPreview)
        val tvFileName = view.findViewById<TextView>(R.id.tvAttachmentFileName)
        val btnRemove = view.findViewById<ImageView>(R.id.ivRemoveAttachment)

        // 파일명 표시
        tvFileName.text = getFileName(uri)
        // 이미지면 썸네일, 아니면 아이콘
        val mimeType = contentResolver.getType(uri)
        if (mimeType != null && mimeType.startsWith("image")) {
            ivPreview.setImageURI(uri)
        } else {
            ivPreview.setImageResource(R.drawable.ic_file) // 파일 아이콘 필요
        }
        btnRemove.setOnClickListener {
            attachmentPreviewLayout.removeView(view)
            attachedFiles.remove(uri)
        }
        attachmentPreviewLayout.addView(view)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "첨부파일"
    }

    private fun uploadPostWithFiles(title: String, content: String, userId: Long) {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getApiService(this@CreatePostActivity)
                if (attachedFiles.isEmpty()) {
                    // 파일 첨부 없을 때는 application/json으로 전송
                    val request = BoardPostRequest(title, content, userId)
                    val response = api.createBoardPost(request)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreatePostActivity, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CreatePostActivity, "게시글 등록에 실패했습니다. (코드: "+response.code()+")", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 파일 첨부 있을 때는 기존 Multipart 방식
                    val titleBody = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
                    val contentBody = RequestBody.create("text/plain".toMediaTypeOrNull(), content)
                    val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())
                    val fileParts = attachedFiles.mapNotNull { uri ->
                        val file = uriToFile(uri)
                        file?.let {
                            val reqFile = it.asRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("files", it.name, reqFile)
                        }
                    }
                    val response = api.createBoardPostWithFiles(titleBody, contentBody, userIdBody, fileParts)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreatePostActivity, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CreatePostActivity, "게시글 등록에 실패했습니다. (코드: "+response.code()+")", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreatePostActivity, "네트워크 오류: "+e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uriToFile(uri: Uri): File? {
        // SAF로 선택한 파일을 임시 파일로 복사
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, getFileName(uri))
            file.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun getUserId(): Long {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("user_id", -1L)
    }
} 